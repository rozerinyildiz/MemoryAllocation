import java.io.*;
import java.util.*;

public class memoryAllocation{


    ArrayList<String> sfmp1 = new ArrayList<>(); // size of free memory.txt partitions
    ArrayList<String> process1 = new ArrayList<>(); // size of processes

    
    // output.txt
     FileWriter fw;

    {
        try {
            fw = new FileWriter("src/output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PrintWriter pw = new PrintWriter(fw);



    //get inputs from memory.txt
    void getinputs() {

        process1.clear(); // restart for each time
        sfmp1.clear();
        try {
            File myObj = new File("src/memory.txt");
            Scanner myReader = new Scanner(myObj);
            int a = 0;
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] dt = data.split(",");
                if (a == 0) {
                    for (int i = 0; i < dt.length; i++) {
                        if (dt[i] != "") {
                            sfmp1.add(dt[i]); // get free inputs
                            // System.out.println(sfmp1.get(i));
                        }
                    }
                    a++;
                } else {
                    for (int j = 0; j < dt.length; j++) {
                        if (dt[j] != "") {
                            process1.add(dt[j]); // get process inputs
                            //  System.out.println(process1.get(j));
                        }
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    void firstFit() throws IOException {

        ArrayList<String> sfmp = new ArrayList<>(); // size of free memory.txt.txt partitions
        ArrayList<String> process = new ArrayList<>(); // size of processes

        sfmp.addAll(sfmp1);
        process.addAll(process1);

        // key : process   ,   value : Arraylist
        Map<String, ArrayList<String>> ffmap = new LinkedHashMap<>();

        ArrayList<String> dfmp = new ArrayList<>();
        dfmp.addAll(sfmp);
        ffmap.put("start", dfmp);


        for (int i = 0; i < process.size(); i++) { // process loop
            for (int j = 0; j < sfmp.size(); j++) { // free loop

                String str = sfmp.get(j);
                char c = str.charAt(str.length() - 1);

                // if process less than
                if (!"*".equals(String.valueOf(c)) && Integer.parseInt(process.get(i)) < Integer.parseInt(sfmp.get(j))) {
                    int d = Integer.parseInt(sfmp.get(j)) - Integer.parseInt(process.get(i));
                    sfmp.remove(sfmp.get(j));
                    sfmp.add(j, process.get(i) + "*");
                    sfmp.add(j + 1, String.valueOf(d));

                    ArrayList<String> fmp1 = new ArrayList<>();
                    fmp1.addAll(sfmp);
                    ffmap.put(process.get(i), fmp1);
                    break;
                    // if process number equals free memory  part number
                } else if (!"*".equals(String.valueOf(c)) && Integer.parseInt(process.get(i)) == Integer.parseInt(sfmp.get(j))) {
                    sfmp.remove(sfmp.get(j));
                    sfmp.add(j, process.get(i) + "*");

                    ArrayList<String> fmp2 = new ArrayList<>();
                    fmp2.addAll(sfmp);
                    ffmap.put(process.get(i), fmp2);
                    break;
                    // if process number greater than all free memoryt part numbers
                } else {
                    for (int m = 0; m < sfmp.size(); m++) { // if there is no element less or equal, it won't allocated
                        if (!"*".equals(String.valueOf(c))) {
                            if (Integer.parseInt(process.get(i)) <= Integer.parseInt(sfmp.get(j))) {
                                continue;
                            }
                        }
                        ArrayList<String> fmp3 = new ArrayList<>();
                        fmp3.add("not allocated, must wait");
                        ffmap.put(process.get(i), fmp3);

                        break;

                    }
                }
            }
        }
   /*     int c=0;
        ArrayList<String> fmp3 = new ArrayList<>();
        fmp3.add("not allocated, must wait");
        for(HashMap.Entry<String, ArrayList<String>> entry : ffmap.entrySet()) {
            if (!entry.getValue().equals(fmp3)) {
                ++c;  // find place of not allocated number
            }else{
                break;
            }
        }

        for (int i = c; i < process.size(); i++) {

            ffmap.remove(process.get(i));
            ffmap.put(process.get(i), fmp3);
        }

    */




// print part
        String str = "";
        myloop:
        for (HashMap.Entry<String, ArrayList<String>> entry : ffmap.entrySet()) {
            str += entry.getKey() + " => ";

            for (int j = 0; j < entry.getValue().size(); j++) {
                str += " " + entry.getValue().get(j);

            }
            str += '\n';
        }
        System.out.print(str);
        System.out.println();

// output.txt  part



    //    FileWriter fw = new FileWriter("src/output.txt");
    //    PrintWriter pw = null;

        try {
        //    pw = new PrintWriter(fw);

            pw.println("************* FIRST - FIT *************** ");

            myloop:
            for (HashMap.Entry<String, ArrayList<String>> entry : ffmap.entrySet()) {
                pw.print(entry.getKey() + " => ");

                for (int j = 0; j < entry.getValue().size(); j++) {

                    pw.print(" " + entry.getValue().get(j));

                    //    if (entry.getValue().get(j).equals("not allocated, must wait")) {

                    //       break;
                    //    }
                }
                pw.println();
            }
            pw.println();
            pw.flush();
        } finally {
            try {

                // always close the writer
               // pw.close();
            } catch (Exception e) {

            }
        }
    }


    void bestFit() throws IOException {

        ArrayList<String> sfmp = new ArrayList<>(); // size of free memory.txt  partitions list
        ArrayList<String> process = new ArrayList<>(); // size of processes list

        sfmp.addAll(sfmp1);
        process.addAll(process1);

        Map<String, ArrayList<String>> ffmap2 = new LinkedHashMap<>();

        ArrayList<String> dfmp = new ArrayList<>();
        dfmp.addAll(sfmp);
        ffmap2.put("start", dfmp); // first line


        for (int i = 0; i < process.size(); i++) {  // process

            int minFree = smallestfree(process.get(i), sfmp); // call smallestfree for getting smallest greater number

            if (minFree != -1 && Integer.parseInt(process.get(i)) != minFree) { // if process and free number not equals
                int diff = minFree - Integer.parseInt(process.get(i));
                int index = sfmp.indexOf(String.valueOf(minFree));

                sfmp.remove(sfmp.get(index));
                sfmp.add(index, process.get(i) + "*");
                sfmp.add(index + 1, String.valueOf(diff));


                ArrayList<String> free1 = new ArrayList<>();
                free1.addAll(sfmp);
                ffmap2.put(process.get(i), free1);

                // if process and free number equals
            } else if (minFree != -1 && Integer.parseInt(process.get(i)) == minFree) {
                int index = sfmp.indexOf(String.valueOf(minFree));

                sfmp.remove(sfmp.get(index));
                sfmp.add(index, String.valueOf(process.get(i)) + "*");

                ArrayList<String> free2 = new ArrayList<>();
                free2.addAll(sfmp);
                ffmap2.put(process.get(i), free2);


            } // not allocated
            if (minFree == -1) {
                int index = sfmp.indexOf(String.valueOf(minFree));

                ArrayList<String> free3 = new ArrayList<>();
                free3.add("not allocated, must wait");

                ffmap2.put(process.get(i), free3);

                //      for (int a = i; a < process.size(); a++) {
                //        ffmap2.put(process.get(a), free3);
                //     }
                // break;
            }
        }

// print part
        String str2 = "";

        myloop:
        for (HashMap.Entry<String, ArrayList<String>> entry : ffmap2.entrySet()) {
            str2 += entry.getKey() + " => ";

            for (int j = 0; j < entry.getValue().size(); j++) {
                str2 += " " + entry.getValue().get(j);

            }
            str2 += '\n';

        }
        System.out.print(str2);
        System.out.println();


        //output.txt
      //  FileWriter fw = new FileWriter("src/output.txt");
      //  PrintWriter pw = null;

        try {
         //   pw = new PrintWriter(fw);

            pw.println("************* BEST - FIT *************** ");

            myloop:
            for (HashMap.Entry<String, ArrayList<String>> entry : ffmap2.entrySet()) {
                pw.print(entry.getKey() + " => ");

                for (int j = 0; j < entry.getValue().size(); j++) {

                    pw.print(" " + entry.getValue().get(j));

                }
                pw.println();
            }
            pw.println();
            pw.flush();
        } finally {
            try {

                // always close the writer
               // pw.close();
            } catch (Exception e) {
            }
        }

    }

    // to find smallest hole that is the big enough
    public static int smallestfree(String process, ArrayList<String> array) {

        int min = Integer.MAX_VALUE; // will be smallest greater number
        int pro = Integer.parseInt(process); // process number
        int minS = Integer.MAX_VALUE;
        for (int i = 0; i < array.size(); i++) {

            if (isInteger(array.get(i))) {
                int freeNum = Integer.parseInt(array.get(i));
                int diff = freeNum - pro;


                if (freeNum == pro) {
                    min = freeNum;
                    break;
                } else if (diff < minS && diff >= 0) {
                    minS = diff;
                    min = freeNum;
                }

            }
        }
        if (min == Integer.MAX_VALUE) {
            return -1;
        } else {
            return min;
        }
    }


    private static boolean isInteger(String s) { //  check string is numeric or not
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    void worstFit() throws IOException {

        ArrayList<String> sfmp = new ArrayList<>(); // size of free memory.txt t partitions
        ArrayList<String> process = new ArrayList<>(); // size of processes

        sfmp.addAll(sfmp1);
        process.addAll(process1);

        Map<String, ArrayList<String>> ffmap3 = new LinkedHashMap<>();

        ArrayList<String> dfmp = new ArrayList<>();
        dfmp.addAll(sfmp);
        ffmap3.put("start", dfmp);


        for (int i = 0; i < process.size(); i++) {  // process

            int maxFree = largestfree(process.get(i), sfmp); // call to find largest one

            // if process and free number not equals
            if (maxFree != -1 && Integer.parseInt(process.get(i)) != maxFree) {
                int diff = maxFree - Integer.parseInt(process.get(i));
                int index = sfmp.indexOf(String.valueOf(maxFree));

                sfmp.remove(sfmp.get(index));
                sfmp.add(index, process.get(i) + "*");
                sfmp.add(index + 1, String.valueOf(diff));


                ArrayList<String> free1 = new ArrayList<>();
                free1.addAll(sfmp);
                ffmap3.put(process.get(i), free1);

                // if they are equals
            } else if (maxFree != -1 && Integer.parseInt(process.get(i)) == maxFree) {
                int index = sfmp.indexOf(String.valueOf(maxFree));

                sfmp.remove(sfmp.get(index));
                sfmp.add(index, String.valueOf(process.get(i)) + "*");

                ArrayList<String> free2 = new ArrayList<>();
                free2.addAll(sfmp);
                ffmap3.put(process.get(i), free2);


            }// if therre is no greater than process
            if (maxFree == -1) {
                int index = sfmp.indexOf(String.valueOf(maxFree));

                ArrayList<String> free3 = new ArrayList<>();
                free3.add("not allocated, must wait");

                ffmap3.put(process.get(i), free3);

                //   for (int a = i; a < process.size(); a++) {
                //      ffmap3.put(process.get(a), free3);
                //  }
                //  break;
            }
        }

// print part
        String str2 = "";
        for (HashMap.Entry<String, ArrayList<String>> entry : ffmap3.entrySet()) {
            str2 += entry.getKey() + " => ";
            for (int j = 0; j < entry.getValue().size(); j++) {
                str2 += " " + entry.getValue().get(j);


            }
            str2 += '\n';
        }
        System.out.print(str2);
        System.out.println();

// output.txt  part
       // FileWriter fw = new FileWriter("src/output.txt");
       // PrintWriter pw = null;

        try {
           // pw = new PrintWriter(fw);

            pw.println("************* WORST - FIT *************** ");

            myloop:
            for (HashMap.Entry<String, ArrayList<String>> entry : ffmap3.entrySet()) {
                pw.print(entry.getKey() + " => ");

                for (int j = 0; j < entry.getValue().size(); j++) {

                    pw.print(" " + entry.getValue().get(j));


                }
                pw.println();
            }

            pw.flush();
        } finally {
            try {
                // always close the writer
                pw.close();
            } catch (Exception e) {

            }
        }
    }

    public static int largestfree(String process, ArrayList<String> array) {

        int max = Integer.MIN_VALUE;
        int pro = Integer.parseInt(process);

        for (int i = 0; i < array.size(); i++) {

            if (isInteger(array.get(i))) { // check if numeric or not
                int freeNum = Integer.parseInt(array.get(i));
                int diff = freeNum - pro;


                if (freeNum > max && diff>=0) { // find max number

                    max = freeNum;

                }
            }
        }
        if (max == Integer.MIN_VALUE) {
            return -1;
        } else {
            return max;
        }
    }

}