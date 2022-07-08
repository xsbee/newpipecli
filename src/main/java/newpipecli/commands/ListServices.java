package newpipecli.commands;

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.Callable;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.StreamingService.ServiceInfo;
import org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandlerFactory;

import picocli.CommandLine.Command;

@Command(name = "list-services", description = "List services and their capabilities")
public class ListServices implements Callable<Integer> {	
	@Override
	public Integer call() throws Exception {
		List<StreamingService> services = NewPipe.getServices();
		
		for (StreamingService service : services) {
			ServiceInfo info = service.getServiceInfo();
			SearchQueryHandlerFactory queryHandler = service.getSearchQHFactory();
			
			String[] contentFilters = queryHandler.getAvailableContentFilter();
			String[] sortFilters = queryHandler.getAvailableSortFilter();
			List<MediaCapability> mediaCaps = info.getMediaCapabilities();
			
			System.out.printf("[%d] %s \n\t"+ 
							  "Filters: %s\n\t"+
							  "Sortings: %s\n\t"+
							  "Types: %s\n",
					
					service.getServiceId(), info.getName(),
					Arrays.toString(contentFilters),
					Arrays.toString(sortFilters),
					mediaCaps
			);
		}
		
		return 0;
	}
}
