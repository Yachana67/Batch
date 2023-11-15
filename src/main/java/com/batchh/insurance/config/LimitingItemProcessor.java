package com.batchh.insurance.config;

import org.springframework.batch.item.ItemProcessor;

import com.batchh.insurance.Model.Insurance;

public class LimitingItemProcessor implements ItemProcessor<Insurance, Insurance> {
	  private static final int MAX_RECORDS = 20;
	    private int count = 0;
	@Override
	public Insurance process(Insurance item) throws Exception {
		if (count < MAX_RECORDS) {
            count++;
           System.out.println("Printing Record: " + item.toString());
            return item;
        } else {
            return null; // Return null to indicate that no more records should be processed
        }
    }
	}


