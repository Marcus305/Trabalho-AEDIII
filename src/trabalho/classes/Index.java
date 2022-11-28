package trabalho.classes;

import java.io.File;
import java.io.RandomAccessFile;

public class Index {

    public void create(int id, long pos) {
        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/index0.txt", "rw")) {
            int hash = this.hash(id);
            int key = hash*20;
            raf.seek(key);
            int idLido;
            try {
                idLido = raf.readInt();
            } catch(Exception e) {
                idLido = 0;
            }
            if(idLido == 0) {
                raf.seek(key);
                raf.writeInt(id);
                raf.writeLong(pos);
                raf.writeLong(-1);
            } else {
                boolean check = false;
                raf.seek(key);
                while(!check) {
                    raf.seek(raf.getFilePointer()+12);
                    long posProx = raf.getFilePointer();
                    long prox = raf.readLong();
                    if(prox == -1) {
                        raf.seek(posProx);
                        raf.writeLong((long)id*20);
                        raf.seek((long)id*20);
                        raf.writeInt(id);
                        raf.writeLong(pos);
                        raf.writeLong(-1);
                        check = true;
                    } else {
                        raf.seek(prox);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int hash(int id) {
        int max = 11;
        return id% max;
    }

    public long read(int id) {
        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/index0.txt", "r")) {
            int hash = this.hash(id);
            int key = hash*20;
            raf.seek(key);
            int idLido;
            try {
                idLido = raf.readInt();
            } catch(Exception e) {
                idLido = 0;
            }
            if(idLido == 0) {
                return -1;
            } else {
                boolean check = false;
                raf.seek(key);
                while(!check) {
                    idLido = raf.readInt();
                    if(idLido == id) {
                        return raf.readLong();
                    } else {
                        raf.seek(raf.getFilePointer()+8);
                        long pos = raf.readLong();
                        if(pos == -1) {
                            return -1;
                        } else {
                            raf.seek(pos);
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public void update(int id, long change) {
        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/index0.txt", "rw")) {
            int hash = this.hash(id);
            int key = hash*20;
            raf.seek(key);
            int idLido;
            try {
                idLido = raf.readInt();
            } catch(Exception e) {
                idLido = 0;
            }
            if(idLido == 0) {
                System.out.println("Este id não existe!");
            } else {
                boolean check = false;
                raf.seek(key);
                while(!check) {
                    idLido = raf.readInt();
                    if(idLido == id) {
                        raf.writeLong(change);
                        check = true;
                    } else {
                        raf.seek(raf.getFilePointer()+8);
                        long pos = raf.readLong();
                        if(pos != -1) {
                            raf.seek(pos);
                        } else {
                            System.out.println("Erro de index");
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimir() {
        try(RandomAccessFile raf = new RandomAccessFile("src/trabalho/main/output/index0.txt", "r")) {
            System.out.println("PH    |    Id    |    Pos    |    Prox");
            for(int i=0; raf.getFilePointer() != raf.length(); i++) {
                int key = i*20;
                System.out.println(key+"    |    "+raf.readInt()+"    |    "+raf.readLong()+"    |    "+raf.readLong());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimir2() {
        String filePath = new File("").getAbsolutePath();
        System.out.println(filePath);
        //filePath.concat("path to the property file");
        /*try(RandomAccessFile raf = new RandomAccessFile("C:\\Users\\marcu\\IdeaProjects\\AEDIII_Trabalho\\Trabalho-AEDIII\\src\\trabalho\\output\\index0.txt", "r")) {
            System.out.println("PH    |    Id    |    Pos    |    Prox");
            for(int i=0; raf.getFilePointer() != raf.length(); i++) {
                int key = i*20;
                System.out.println(key+"    |    "+raf.readInt()+"    |    "+raf.readLong()+"    |    "+raf.readLong());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }*/
    }

}
