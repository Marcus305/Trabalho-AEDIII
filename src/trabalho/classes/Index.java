package trabalho.classes;

import java.io.RandomAccessFile;

public class Index {
    private int max = 101;

    public void create(int id, long pos) {
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\index0.txt", "rw")) {
            int key = hash(id);
            boolean check = false;
            raf.seek(12*(long)key);
            try{
                raf.readInt();
            } catch(Exception e) {
                check = true;
            }
            if(check) {
                raf.seek(12*(long)key);
            }
            raf.seek(raf.length());
            raf.writeInt(id);
            raf.writeLong(pos);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int hash(int id) {
        return id%this.max;
    }

    public long find(int id) {
        int key = hash(id);
        long pos = 0;
        try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\src\\trabalho\\output\\index0.txt", "rw")) {
            raf.seek(raf.getFilePointer());
            raf.seek(12*(long)key);
            raf.readInt();
            pos = raf.readLong();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return pos;
    }
}
