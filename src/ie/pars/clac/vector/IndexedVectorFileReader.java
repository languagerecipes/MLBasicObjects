/* 
 * Copyright 2016 Behrang QasemiZadeh <zadeh at phil.hhu.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ie.pars.clac.vector;

import com.jramoyo.io.BufferedRandomAccessFile;
import com.jramoyo.io.FileIndexingException;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class IndexedVectorFileReader implements Closeable, AutoCloseable {
    // Shared across all instances

    private static final ForkJoinPool DEFAULT_POOL = new ForkJoinPool();

    private static final long MIN_FORK_THRESHOLD = 1000000L;
    private static final String READ_MODE = "r";

    private final BufferedRandomAccessFile raf;
    private final Charset charset;
    //private final SortedSet<Long> index;

    private final Map<String, Long> index;
    private final Lock lock;

    private boolean isClosed = false;

    /**
     * Read the file and index it for keys that are separated from the content
     * using the specified key
     *
     * @param file
     * @param Delimiter
     * @throws IOException
     */
    public IndexedVectorFileReader(File file, String Delimiter) throws IOException {
        this(file, Charset.defaultCharset(), 1, DEFAULT_POOL, Delimiter);
    }

    public IndexedVectorFileReader(File file, Charset charset, String Delimiter) throws IOException {
        this(file, charset, 1, DEFAULT_POOL, Delimiter);
    }

    public IndexedVectorFileReader(File file, Charset charset, int splitCount, String Delimiter)
            throws IOException {

        this(file, charset, splitCount, DEFAULT_POOL, Delimiter);
    }

    /**
     * Creates a IndexedFileReader, given the <code>File</code> to read from.
     * <p>
     * The specified character set will be used and the file will be
     * concurrently split according to the specified <code>splitCount</code>.
     * The specified pool will be used to concurrently index the file.
     * </p>
     *
     * @param file the <code>File</code> to read from
     * @param charset the character set to use
     * @param splitCount the number of times the file will be divided during
     * concurrent indexing
     * @param pool the pool to use when concurrently indexing the file
     * @param Delimiter
     * @throws IOException
     */
    public IndexedVectorFileReader(File file, Charset charset, int splitCount,
            ForkJoinPool pool, String Delimiter) throws IOException {
        this.raf = new BufferedRandomAccessFile(file, READ_MODE);
        this.charset = charset;

        long threshold = Math.max(MIN_FORK_THRESHOLD, file.length()
                / splitCount);
        IndexingTask indexingTask = new IndexingTask(file, Delimiter, 0, file.length(), threshold);
        index = Collections.unmodifiableMap(pool.invoke(indexingTask));
        this.lock = new ReentrantLock();
    }

    /**
     * Creates a IndexedTextFileReader, given the <code>File</code> to read
     * from.
     * <p>
     * The default character set will be used and the file will be concurrently
     * split according to the specified <code>splitCount</code>. The default
     * pool will be used to concurrently index the file.
     * </p>
     *
     * @param file the <code>File</code> to read from
     * @param splitCount the number of times the file will be divided during
     * concurrent indexing
     * @param delimiter
     * @throws IOException
     */
    public IndexedVectorFileReader(File file, int splitCount, String delimiter) throws IOException {
        this(file, Charset.defaultCharset(), splitCount, DEFAULT_POOL, delimiter);
    }

    @Override
    public void close() throws IOException {
        
        raf.close();
        isClosed = true;
    }

    public boolean hasEntry(String key){
        return  this.index.containsKey(key);
    }
    public String readLine(String key)
            throws IOException {
        assertNotClosed();
        try {
            lock.lock();

            long keyPosition = this.index.get(key);
            //index.positions.subSet(keyPosition, lineNumber+1);
            raf.seek(keyPosition);

            String line = raf.getNextLine(charset);

            return line;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the number of indexed lines.
     *
     * @return the number of indexed lines.
     */
    public int getLineCount() {
        return index.size();
    }

    public Set<String> entrySet() {
        return this.index.keySet();
    }

    private void assertNotClosed() {
        if (isClosed) {
            throw new IllegalStateException("Reader is closed!");
        }
    }

    /**
     * IndexingTask
     * <p>
     * Forked task for indexing text files.
     * </p>
     *
     * @author jramoyo
     */
    private static final class IndexingTask extends
            RecursiveTask<TreeMap<String, Long>> {

        private static final long serialVersionUID = 3509549890190032574L;

        private final File file;
        private final long start;
        private final long end;
        private final long length;
        private final long threshold;
        private String DELIMITER;

        /**
         * Creates a IndexingTask
         *
         * @param file the file to index
         * @param start the starting offset
         * @param end the end offset
         * @param threshold the threshold used to decide whether to compute
         * directly or fork to another task
         */
        public IndexingTask(File file, String DELIMITER, long start, long end, long threshold) {
            this.DELIMITER = DELIMITER;
            this.file = file;
            this.start = start;
            this.end = end;
            this.length = end - start;
            this.threshold = threshold;
        }

        /**
         * The forked computation.
         * <p>
         * The resulting index always includes the position to the end-of-file
         * (EOF).
         * </p>
         *
         * @return a Sorted set of positions representing a start of line.
         */
        @Override
        protected TreeMap<String, Long> compute() {
            TreeMap<String, Long> index = new TreeMap<>();
            try {
                if (length < threshold) {
                    BufferedRandomAccessFile raf = null;
                    try {
                        raf = new BufferedRandomAccessFile(file, "r");
                        raf.seek(start);

                        // Add the position for 1st line
                        if (raf.getFilePointer() == 0L) {
                            String line = raf.getNextLine();
                            String key = line.split(DELIMITER, 2)[0];
                            index.put(key, raf.getFilePointer());

                        }
                        while (raf.getFilePointer() < end) {
                            long currentPosition = raf.getFilePointer();
                            String line = raf.getNextLine();
                            String key = line.split(DELIMITER, 2)[0];
                            index.put(key, currentPosition);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(IndexedVectorFileReader.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (raf != null) {
                            raf.close();
                        }
                    }
                } else {
                    long start1 = start;
                    long end1 = start + (length / 2);

                    long start2 = end1;
                    long end2 = end;

                    IndexingTask task1 = new IndexingTask(file, DELIMITER, start1, end1,
                            threshold);
                    task1.fork();
                    IndexingTask task2 = new IndexingTask(file, DELIMITER, start2, end2,
                            threshold);

                    index.putAll(task2.compute());
                    index.putAll(task1.join());
                }
            } catch (IOException ex) {
                throw new FileIndexingException(file, ex);
            }

            return index;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        this.raf.close();
    }
    
    
    
}
