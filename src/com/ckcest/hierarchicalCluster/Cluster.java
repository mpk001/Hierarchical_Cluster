package com.ckcest.hierarchicalCluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster {  
    private List<Integer> dataPoints = new ArrayList<>(); 
    private String clusterName;  
  
    public List<Integer> getDataPoints() {  
        return dataPoints;  
    }  
  
    public void setDataPoints(List<Integer> dataPoints) {  
        this.dataPoints = dataPoints;  
    }  
  
    public String getClusterName() {  
        return clusterName;  
    }  
  
    public void setClusterName(String clusterName) {  
        this.clusterName = clusterName;  
    } 
}
