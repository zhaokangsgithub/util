package cn.enjoyedu.ch2.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * ClassName:  <br/>
 * Function:  通过文件名和需要查询的盘符，查询文件路径
 *
 * @author
 * @since JDK 1.6
 */
public class FindFileUtil
{
    private static class FindFileAction<V> extends RecursiveAction
    {

        /**
         * 查询的路径
         */
        private File file;

        /**
         * 查询的文件名
         */
        private String fileName;

        public FindFileAction(V path, String fileName)
        {
            if (path instanceof File)
            {
                this.file = (File)path;
            }
            else
            {
                this.file = new File(path.toString());
            }
            this.fileName = fileName;
        }

        @Override
        protected void compute()
        {
            List<FindFileAction> tasks = new ArrayList();
            if (file != null)
            {
                File[] files = file.listFiles();
                if (files != null)
                {

                    for (File innerFile : files)
                    {
                        if (innerFile.isDirectory())
                        {
                            tasks.add(new FindFileAction(innerFile, fileName));
                        }
                        else
                        {
                            String absolutePath = innerFile.getAbsolutePath();
                            if (absolutePath.toUpperCase().contains(fileName.toUpperCase()))
                            {
                                System.out.println(absolutePath);
                            }
                        }
                    }
                }
                invokeAll(tasks);
            }
        }
    }

    public static void main(String[] args)
    {
        ForkJoinPool pool = new ForkJoinPool();
        FindFileAction<String> objectFindFileAction = new FindFileAction<>("D:\\", ".jmx");
        pool.invoke(objectFindFileAction);
    }

}
