package walkingtechie.jobs;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import walkingtechie.model.Employee;
import walkingtechie.orm.JpaQueryProviderImpl;

@Configuration
@EnableBatchProcessing
@EntityScan("walkingtechie.model")
public class ReadFromDB {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public Job readEmployee() {
		return jobBuilderFactory.get("readEmployee").flow(step1()).end().build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").allowStartIfComplete(true).<Employee,Employee>chunk(1).reader(reader()).writer(writer()).build();
	}
	
	@Bean
	public JpaPagingItemReader<Employee> reader() {
		JpaPagingItemReader<Employee> pagingItemReader = new JpaPagingItemReader<Employee>();
		pagingItemReader.setEntityManagerFactory(entityManagerFactory);
		JpaQueryProviderImpl<Employee> jpaQueryProvider = new JpaQueryProviderImpl<Employee>();
		jpaQueryProvider.setQuery("Employee.findAll");
		pagingItemReader.setQueryProvider(jpaQueryProvider);
		pagingItemReader.setPageSize(1000);
		
		try {
			pagingItemReader.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingItemReader;
	}
	
	@Bean
	public FlatFileItemWriter<Employee> writer() {
		FlatFileItemWriter<Employee> flatItemWriter = new FlatFileItemWriter<Employee>();
		flatItemWriter.setResource(new FileSystemResource("Output/Employee.csv"));
		
		DelimitedLineAggregator<Employee> delimitedLineAggregator = new DelimitedLineAggregator<Employee>();
		delimitedLineAggregator.setDelimiter("|");
		BeanWrapperFieldExtractor<Employee> fieldExtractor = new BeanWrapperFieldExtractor<Employee>();
		fieldExtractor.setNames(new String[]{"id", "employeeName", "checkIn", "checkOut"});
		delimitedLineAggregator.setFieldExtractor(fieldExtractor);
		
		flatItemWriter.setLineAggregator(delimitedLineAggregator);
		return flatItemWriter;
	}
	
	/*
	 * @Bean public StaxEventItemWriter<Employee> writer() {
	 * StaxEventItemWriter<Employee> itemWriter = new
	 * StaxEventItemWriter<Employee>(); itemWriter.setResource(new
	 * FileSystemResource("Output/Employee.xml"));
	 * itemWriter.setMarshaller(employeeUnMarshaller());
	 * itemWriter.setRootTagName("Employees"); return itemWriter; }
	 * 
	 * @Bean public XStreamMarshaller employeeUnMarshaller() { XStreamMarshaller
	 * unMarshaller = new XStreamMarshaller(); HashMap<String, Class<Employee>>
	 * aliases = new HashMap<String, Class<Employee>>(); aliases.put("employee",
	 * Employee.class); unMarshaller.setAliasesByType(aliases); return unMarshaller;
	 * }
	 */
}
