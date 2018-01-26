package com.test.sim;

import java.util.List;

import com.test.sim.model.Plane;

public class Simulator {

	public static void main(String... args) throws Exception {
    	List<Plane> planes = Config.readData(args.length > 0 ? args[0] : null);
    	planes.forEach(System.out::println);
    }	
}