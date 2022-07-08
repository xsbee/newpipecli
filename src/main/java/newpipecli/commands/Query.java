package newpipecli.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.Callable;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.SearchExtractor;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "query", description = "Query a search term with filtered results")
public class Query implements Callable<Integer> {
	
	@Parameters(index = "0",
				description = "Search term to query")
	String query;
	
	@Option(names = {"-s", "--search"}, 
			defaultValue = "YouTube",
			description = "Name of the service to search in")
	String serviceName;
	
	@Option(names = {"-f", "--filters"}, description = "Filters to apply in query")
	String[] filters;
	
	@Option(names = "--sorting", description = "Sorting to apply in query")
	String sorting;
	
	@Override
	public Integer call() throws Exception {
		StreamingService service;
		
		// TODO log it via a proper facility
		try {
			service = NewPipe.getService(serviceName);
		} catch (ExtractionException e) {
			System.err.println(e.getLocalizedMessage());
			
			return -1;
		}
		
		// TODO validate filters and sorting, NewPipe silently allows through
		// without checking validity. Possibly a bug!
		List<String> filterList = filters == null 
				? Collections.emptyList() 
				: Arrays.asList(filters);
		SearchExtractor search = service.getSearchExtractor(query, filterList, sorting);
		
		search.fetchPage();
		
		// TODO use pagination for better UX
		for (InfoItem item : search.getInitialPage().getItems()) {
			System.out.printf("%s: %s\n", item.getName(), item.getUrl());
		}
			
		return 0;
	}
}
