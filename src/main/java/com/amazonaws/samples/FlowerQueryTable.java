/**
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
*/



package com.amazonaws.samples;

import java.util.HashMap;
import java.util.Iterator;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

public class FlowerQueryTable {

    public static void iterateSeason (String whichSeason, Table table) throws Exception {
    	HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#season", "season");
   
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":ssss", whichSeason);
        

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#season = :ssss").withNameMap(nameMap)
            .withValueMap(valueMap);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println(whichSeason + " season flowers:");
            items = table.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getString("season") + ": " + item.getString("type"));
            }

        }
        catch (Exception e) {
            System.err.println("Unable to query flowers from spring");
            System.err.println(e.getMessage());
        }

    }
    
	public static void main(String[] args) throws Exception {

    	ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/johnmortensen/.aws/credentials), and is in valid format.",
                    e);
        }
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        	.withCredentials(credentialsProvider)
            .withRegion("us-east-2")
            .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Flowers");

        iterateSeason ("spring", table);
        iterateSeason ("summer", table);
        iterateSeason ("fall", table);
        iterateSeason ("winter", table);
 
        
        
        
        
        
        
        
        
        
//        valueMap.put(":ssss", "winter");
//        valueMap.put(":letter1", "A");
//        valueMap.put(":letter2", "L");
//
//        querySpec.withProjectionExpression("#season, item.type, info.desctiption, info.color")
//            .withKeyConditionExpression("#season = :ssss and type between :letter1 and :letter2").withNameMap(nameMap)
//            .withValueMap(valueMap);
//
//        try {
//            System.out.println("Flowers from spring to winter - titles A-L, with type and description");
//            items = table.query(querySpec);
//
//            iterator = items.iterator();
//            while (iterator.hasNext()) {
//                item = iterator.next();
//                System.out.println(item.getString("season") + ": " + item.getString("type") + " " + item.getMap("description"));
//            }
//
//        }
//        catch (Exception e) {
//            System.err.println("Unable to query flowers in winter:");
//            System.err.println(e.getMessage());
//        }
//    }
}}
