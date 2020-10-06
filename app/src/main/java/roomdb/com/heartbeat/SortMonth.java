package com.heartbeat;

import java.util.Comparator;

public class SortMonth implements Comparator<HeartBeat> {

    @Override
    public int compare(HeartBeat o1, HeartBeat o2) {
      return  o1.getMonth()-o2.getMonth();
    }
}
