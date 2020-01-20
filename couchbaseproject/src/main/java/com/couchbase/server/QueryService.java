package com.couchbase.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

public class QueryService {

	static Bucket bucket = QueryServer.getBucket();

	public static final int RETRY_COUNT = 3;

	public static void executeSelectQuery(String query) {

		bucket.bucketManager().createN1qlPrimaryIndex(true, false);

		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));

		System.out.println("Query to be Executed : " + query);

		if (!result.status().equalsIgnoreCase("success")) {

			Integer errorCode = (Integer) result.errors().get(0).get("code");

			if (errorCode == 11) {
				int retry = 1;

				//Retrying 3 times if we get error code 11
				while (retry <= RETRY_COUNT && !result.status().equalsIgnoreCase("success")) {
					System.out.println("Retrying " + retry + " time");
					result = bucket.query(N1qlQuery.simple(query));
					retry++;
				}
			} else {
				System.err.println("Error While Executing the query :" + result.errors().get(0));
			}

		}

		for (N1qlQueryRow row : result) {
			System.out.println(row);
		}
	}

	public static void main(String args[]) {

		CommandLineParser commandLineParser = new DefaultParser();

		HelpFormatter helpFormatter = new HelpFormatter();

		CommandLine commandLine;

		Options options = getOptions();

		String conditions = null;

		String fields = "*";
		
		String bucketName= bucket.name();

		try {

			commandLine = commandLineParser.parse(options, args);

			if (commandLine.hasOption("f")) {

				fields = commandLine.getOptionValue("f");

				System.out.println("Fields to be display as Query Output: " + fields);

			}

			if (commandLine.hasOption("c")) {

				conditions = commandLine.getOptionValue("c");

				System.out.println("Query Conditions : " + conditions);

			}

			StringBuilder query = new StringBuilder("select " + fields + " from `" + bucketName +"`");

			if (conditions != null && conditions.trim().length() != 0) {

				query.append(" where " + conditions);
			}

			executeSelectQuery(query.toString());

		} catch (ParseException e) {

			System.out.println(e.getMessage());

			helpFormatter.printHelp("QueryService", options);

			System.exit(1);

			return;

		} catch (Exception exception) {

			System.err.println("Error While executing the QueryService : " + exception.getMessage());
		}

	}

	public static Options getOptions() {

		Options options = new Options();

		Option queryOption1 = new Option("f", "fields", true, "Fields to Query");
		queryOption1.setRequired(false);
		queryOption1.setArgName("FIELDS");
		queryOption1.setType(String.class);
		options.addOption(queryOption1);

		Option queryOption2 = new Option("c", "conditions", true, "Conditions to Query");
		queryOption2.setRequired(false);
		queryOption2.setArgName("CONDITIONS");
		queryOption2.setType(String.class);
		options.addOption(queryOption2);

		return options;
	}
}
