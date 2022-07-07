package newpipecli;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.StreamingService.ServiceInfo;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.SearchExtractor;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(mixinStandardHelpOptions = true)
class Main implements Callable<Integer> {
	@Command(description = "Searches for a query")
	void query(
			@Parameters(index = "0")
			String query,
			
			@Option(names = {"-i", "--id"}, defaultValue = "0")
			Integer serviceId  
	) throws ExtractionException, IOException
	{
		StreamingService service = NewPipe.getService(serviceId);
		SearchExtractor search = service.getSearchExtractor(query);
		
		search.fetchPage();
		
		for (InfoItem item : search.getInitialPage().getItems()) {
			System.out.printf("%s: \n\t%s\n\t%s\n", item.getName(), item.getUrl(), item.getThumbnailUrl());
		}
	}
	
	@Command(description = "Lists all streaming services")
	void listservices() {
		List<StreamingService> services = NewPipe.getServices();
		
		for (StreamingService service : services) {
			ServiceInfo info = service.getServiceInfo();
			
			System.out.printf("[%d] %s (%s) \n\t" +
							  "Has: %s\n\t" +
							  "Supports: %s\n",
					service.getServiceId(),
					info.getName(), 
					service.getBaseUrl(),
					Utils.toString(",", info.getMediaCapabilities()).get(),
					Utils.toString(",", service.getSupportedCountries()).orElse("N/A"));
		}
	}

	public static void main(String... args) {
		DownloaderImpl.init(null);
		NewPipe.init(DownloaderImpl.getInstance());
		
		CommandLine main = new CommandLine(new Main());
		System.exit(main.execute(args));
	}

	@Override
	public Integer call() throws Exception {
		return 0;
	}
}