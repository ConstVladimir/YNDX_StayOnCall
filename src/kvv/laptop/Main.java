package kvv.laptop;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        final int n = 3;
        int [][] st = {{1,1,1},{4,4,2},{7,1,1}};
        Station start = new Station(0,0,0);//используем радиус 0 для человека
        Station finish = new Station(8,0,0);

        Set<Station> stations = Arrays.stream(st).map(t->new Station(t[0],t[1],t[2])).collect(Collectors.toSet());
        Set<Station> startSt = start.listIsIntersect(stations); //станции на стартее
        Set<Station> finishSt = finish.listIsIntersect(stations); //на финише

        if (startSt.isEmpty() || finishSt.isEmpty()) System.out.println("0"); //наличие вышки на старте и финише
        else {
            int i = 1;
            Set<Station> GrStartNeighbors = new HashSet<>(startSt);
            Set<Station> GrFinishNeighbors = new HashSet<>(finishSt);
            Set<Station> startNeighbors = startSt;
            Set<Station> finishNeighbors = finishSt;
            while (true){
                if (GrStartNeighbors.removeAll(GrFinishNeighbors)) {System.out.println("1"); break;} //старт и финиш соединены вышками
                else {
                    stations.removeAll(startNeighbors); //удаляем из общего списка вышки которые были определены граничащими в предыдущем цикле
                    stations.removeAll(finishNeighbors); //тоже самое с финишными
                    startNeighbors = Station.listIsIntersectOfGroup(startNeighbors,stations);//список новых граничащих с предыдущими
                    finishNeighbors = Station.listIsIntersectOfGroup(finishNeighbors,stations);// у финишных
                    GrStartNeighbors.addAll(startNeighbors);
                    GrFinishNeighbors.addAll(finishNeighbors);
                    if (startNeighbors.isEmpty() || finishNeighbors.isEmpty()) {System.out.println("0"); break;} //если новых соседних вышек не обнаружится
                }
                System.out.println("degree of neighbors " + i++);
            }
        }
        System.out.println("end");
    }
}

class Station {
    private final int Xc;
    private final int Yc;
    private final int Ax;
    private final int Ay;
    private final int Bx;
    private final int By;
    private final int Cx;
    private final int Cy;
    private final int Dx;
    private final int Dy;

    Station(int x,int y, int r){
        Xc=x;
        Yc=y;
        Ax=x-r;
        Ay=y+r;
        Bx=x+r;
        By=y+r;
        Cx=x+r;
        Cy=y-r;
        Dx=x-r;
        Dy=y-r;
    }

    boolean isIntersect (Station st){
        if (this.Xc > st.Xc){ //входная слева
            if (this.Yc > st.Yc){ // слева снизу
                return this.Dx <= st.Bx && this.Dy <= st.By;
            }
            else {// слева сверху
                return this.Ax <= st.Cx && this.Ay >= st.Cy;
            }
        }
        else{ // входная справа
            if (this.Yc < st.Yc){ // справа сверху
                return this.Bx >= st.Dx && this.By >= st.Dy;
            }
            else {// справа снизу
                return this.Cx >= st.Ax && this.Cy <= st.Ay;
            }
        }
    }

    Set<Station> listIsIntersect (Set<Station> stationList){
        return stationList.stream().filter(t->this.isIntersect(t)).collect(Collectors.toSet());
    }

    static Set<Station> listIsIntersectOfGroup (Set<Station> st_whom, Set<Station> st_which){
        return st_whom.stream().map(t->t.listIsIntersect(st_which)).reduce(new HashSet<Station>(),(s1,s2)->{s1.addAll(s2); return s1; });
    }
}