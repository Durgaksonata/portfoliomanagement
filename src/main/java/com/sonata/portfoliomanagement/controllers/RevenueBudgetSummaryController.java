package com.sonata.portfoliomanagement.controllers;

import java.util.*;
import java.util.stream.Collectors;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;
import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("revenuebudgets")
public class RevenueBudgetSummaryController {
	
	@Autowired
	RevenueBudgetSummaryRepository revenueRepo;
	@Autowired
	AccountBudgetsRepository accountBudgetsRepo;
	@Autowired
	DataEntryRepository dataEntryRepo;

	RestTemplate restTemplate = new RestTemplate();

	//method to add data into revenue table
	@PostMapping("/save")
	public ResponseEntity<RevenueBudgetSummary> createRevenueBudgetSummary(@RequestBody RevenueBudgetSummary revenue) {
	   
	    RevenueBudgetSummary createdRevenue = revenueRepo.save(revenue);
	    
	    return new ResponseEntity<>(createdRevenue, HttpStatus.CREATED);
	}

	@GetMapping("/get")
	public ResponseEntity<List<RevenueBudgetSummary>> getAllRevenueBudgets() {
		List<RevenueBudgetSummary> allRevenueBudgets = revenueRepo.findAll();
		return ResponseEntity.ok(allRevenueBudgets);
	}

	@GetMapping("/{id}")
	public RevenueBudgetSummary getbyid(@PathVariable int id) {
		return revenueRepo.findById(id).get();

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteRevenueVsBudgetById(@PathVariable int id) {
		if (revenueRepo.existsById(id)) {
			revenueRepo.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//method to get only the list of DeliveryManagers present-->
	@GetMapping("/getdmlist")
	public List<String> getdeliveryManagers ()
	{
		List<String> dmList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			dmList.add(request.getDeliveryManager());
		}
		return dmList.stream().distinct().collect(Collectors.toList());
	}



	@GetMapping("/DMs/{dmList}")
	public List<RevenueBudgetSummary> getRevenuebyDM(@PathVariable List<String> dmList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the delivery manager list and filter Revenue objects accordingly
		for (String dm : dmList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByDeliveryManager(dm);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//getting list for all accounts only-->
	@GetMapping("/getaccountlist")
	public List<String> getAccounts ()
	{
		List<String> accountList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			accountList.add(request.getAccount());
		}
		return accountList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/Accounts/{accountList}")
	public List<RevenueBudgetSummary> getRevenuebyAccount(@PathVariable List<String> accountList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the account list and filter Revenue objects accordingly
		for (String account : accountList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByAccount(account);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//getting list for all project Managers only-->
	@GetMapping("/getpmlist")
	public List<String> getProjectManager ()
	{
		List<String> pmList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			pmList.add(request.getProjectManager());
		}
		return pmList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/pms/{pmList}")
	public List<RevenueBudgetSummary> getRevenuebyPM(@PathVariable List<String> pmList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the project manager list and filter Revenue objects accordingly
		for (String pm : pmList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByProjectManager(pm);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//getting list for all vertical only-->
	@GetMapping("/getverticallist")
	public List<String> getVerticalList()
	{
		List<String> verticalList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			verticalList.add(request.getVertical());
		}
		return verticalList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/verticals/{verticalList}")
	public List<RevenueBudgetSummary> getRevenueByVertical(@PathVariable List<String> verticalList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the vertical list and filter Revenue objects accordingly
		for (String vertical : verticalList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByVertical(vertical);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//List of all methods along with  all the data

	//getting classification list-->
	@GetMapping("/getclassificationlist")
	public List<String> getClassificationList()
	{
		List<String> classficationList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			classficationList.add(request.getClassification());
		}
		return classficationList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/classifications/{classificationList}")
	public List<RevenueBudgetSummary> getRevenuebyClassification(@PathVariable List<String> classificationList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the classification list and filter Revenue objects accordingly
		for (String classification : classificationList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByClassification(classification);
			resultList.addAll(filteredList);
		}

		return resultList;
	}



	//getting list for all projects only-->
	@GetMapping("/getprojectlist")
	public List<String> getProjectList()
	{
		List<String> projectList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			projectList.add(request.getProjectName());
		}
		return projectList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/projects/{projectList}")
	public List<RevenueBudgetSummary> getRevenueByProject(@PathVariable List<String> projectList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the project list and filter Revenue objects accordingly
		for (String project : projectList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByProjectName(project);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//getting list for all years only-->
	@GetMapping("/getfinancialyearlist")
	public List<Integer> getFinancialYearList() {
	    List<Integer> fyList = new ArrayList<>();
	    List<RevenueBudgetSummary> revenueData = revenueRepo.findAll();
	    for (RevenueBudgetSummary request : revenueData) {
	        fyList.add(request.getFinancialYear());
	    }
	    return fyList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/FYs/{yearsList}")
	public List<RevenueBudgetSummary> getRevenueByFY(@PathVariable List<Integer> yearsList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the year list and filter Revenue objects accordingly
		for (Integer year : yearsList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByFinancialYear(year);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//get all quarters-->
	@GetMapping("/getquarterlist")
	public List<String> getQuarterList()
	{
		List<String> quarterList = new ArrayList<String>();
		List<RevenueBudgetSummary> revenueData= revenueRepo.findAll();
		for(RevenueBudgetSummary request : revenueData ) {
			quarterList.add(request.getQuarter());
		}
		return quarterList.stream().distinct().collect(Collectors.toList());
	}


	@GetMapping("/quarters/{quarterList}")
	public List<RevenueBudgetSummary> getRevenueByQuarter(@PathVariable List<String> quarterList) {
		// Create a list to store Revenue objects
		List<RevenueBudgetSummary> resultList = new ArrayList<>();

		// Iterate over the quarter list and filter Revenue objects accordingly
		for (String quarter : quarterList) {
			List<RevenueBudgetSummary> filteredList = revenueRepo.findByQuarter(quarter);
			resultList.addAll(filteredList);
		}

		return resultList;
	}


	//get all Months-->
	@GetMapping("/getmonthlist")
	public List<Date> getMonth() {
	    List<Date> monthList = new ArrayList<>();
	    List<RevenueBudgetSummary> revenueData = revenueRepo.findAll();
	    for (RevenueBudgetSummary request : revenueData) {
	        monthList.add(request.getMonth());
	    }
	    return monthList.stream().distinct().collect(Collectors.toList());
	}

	//this method is going to add multiple years,accounts for a dm into a single array object instead of adding each account as a seperate object within the list-->
	@GetMapping("/ultraFilter")
	public ResponseEntity<List<RevenueBudgetSummary>> getByDMAccountYear(@RequestBody RevenueDTO dmList) {
		List<String> dmNames = dmList.getMyList();
		List<RevenueBudgetSummary> responseList = new ArrayList<>();

		for (String dmName : dmNames) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByDeliveryManager(dmName);
			responseList.addAll(revenues);
		}
		return ResponseEntity.ok(responseList);
	}

	//this method is getting the list of accounts only by giving the dm names in the request body in postman.this list to be sent in the next method connecting to it.




	@GetMapping("/accountbydm/{dmList}")
	public ResponseEntity<Set<String>> getAccountByDM(@PathVariable List<String> dmList) {
		Set<String> accountNames = new HashSet<>();

		for (String dmName : dmList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByDeliveryManager(dmName);
			for (RevenueBudgetSummary revenue : revenues) {
				accountNames.add(revenue.getAccount());
			}
		}
		return ResponseEntity.ok(accountNames);
	}


	@GetMapping("/projectbypm/{pmList}")
	public ResponseEntity<Set<String>> getProjectByPM(@PathVariable List<String> pmList) {
		Set<String> projects = new HashSet<>();

		for (String pmName : pmList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByProjectManager(pmName);
			for (RevenueBudgetSummary revenue : revenues) {
				projects.add(revenue.getProjectName());
			}
		}

		return ResponseEntity.ok(projects);
	}

	@GetMapping("/pmbyaccount/{accountList}")
	public ResponseEntity<Set<String>> getPMByAccount(@PathVariable List<String> accountList){
		Set<String> projectManagers = new HashSet<>();

		for (String accountName : accountList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByAccount(accountName);
			for (RevenueBudgetSummary revenue : revenues) {
				projectManagers.add(revenue.getProjectManager());
			}
		}

		return ResponseEntity.ok(projectManagers);
	}

	@GetMapping("/dmbyclassification/{classificationList}")
	public ResponseEntity<Set<String>> getDMByClassification(@PathVariable List<String> classificationList) {
		Set<String> dmNames = new HashSet<>();

		for (String classificationName : classificationList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByClassification(classificationName);
			for (RevenueBudgetSummary revenue : revenues) {
				dmNames.add(revenue.getDeliveryManager());
			}
		}

		return ResponseEntity.ok(dmNames);
	}

	@GetMapping("/financialYearByProject/{projectList}")
	public ResponseEntity<Set<Integer>> getFinancialYearByProject(@PathVariable List<String> projectList) {
		Set<Integer> financialYears = new HashSet<>();

		for (String projectName : projectList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByProjectName(projectName);
			for (RevenueBudgetSummary revenue : revenues) {
				financialYears.add(revenue.getFinancialYear());
			}
		}

		return ResponseEntity.ok(financialYears);
	}

	@GetMapping("/dmbyvertical&classification/{verticalList}/{classificationList}")
	public ResponseEntity<Set<String>> getDMByVerticalAndClassification(@PathVariable List<String> verticalList, @PathVariable List<String> classificationList) {
		Set<String> deliveryManagerNames = new HashSet<>();

		for (String vertical : verticalList) {
			for (String classification : classificationList) {
				List<RevenueBudgetSummary> revenues = revenueRepo.findByVerticalAndClassification(vertical, classification);
				for (RevenueBudgetSummary revenue : revenues) {
					deliveryManagerNames.add(revenue.getDeliveryManager());
				}
			}
		}

		return ResponseEntity.ok(deliveryManagerNames);
	}

	@GetMapping("/dmbyvertical/{verticalList}")
	public ResponseEntity<Set<String>> getDMByVertical(@PathVariable List<String> verticalList) {
		Set<String> deliveryManagerNames = new HashSet<>();

		for (String vertical : verticalList) {
			List<RevenueBudgetSummary> revenues = revenueRepo.findByVertical(vertical);
			for (RevenueBudgetSummary revenue : revenues) {
				deliveryManagerNames.add(revenue.getDeliveryManager());
			}
		}

		return ResponseEntity.ok(deliveryManagerNames);
	}






	@GetMapping("/financialYearByCriteria")
	public ResponseEntity<List<RevenueBudgetSummary>> getFinancialDataByCriteria(
			@RequestParam(value = "verticalList", required = false) List<String> verticalList,
			@RequestParam(value = "classificationList", required = false) List<String> classificationList,
			@RequestParam(value = "dmList", required = false) List<String> dmList,
			@RequestParam(value = "accountList", required = false) String accountList,
			@RequestParam(value = "pmList", required = false) List<String> pmList,
			@RequestParam(value = "projectList", required = false) List<String> projectList,
			@RequestParam(value = "financialYear", required = false) List<Integer> financialYear) {

		// Check if any query parameter has been provided
		if (verticalList == null || classificationList == null || dmList == null ||
				accountList == null || pmList == null || projectList == null || financialYear == null) {
			// If any parameter is missing, return bad request
			return ResponseEntity.badRequest().build();
		}

		RevenueDTO criteria = new RevenueDTO();
		criteria.setVerticalList(verticalList);
		criteria.setClassificationList(classificationList);
		criteria.setDmList(dmList);

		// Handle accountList
		if (accountList != null) {
			List<String> accountListValues = Arrays.asList(accountList.split(","));
			criteria.setAccountList(accountListValues);

		} else {
			criteria.setAccountList(Collections.emptyList());
		}

		criteria.setPmList(pmList);
		criteria.setProjectList(projectList);
		criteria.setFinancialYear(financialYear);

		List<RevenueBudgetSummary> financialData = null;

		// Fetch data only if all criteria are provided and match
		if (criteria.areAllCriteriaMatched()) {
			financialData = fetchData(criteria);
		} else {
			// Indicate that the specified data doesn't exist
			financialData = Collections.emptyList(); // Or you can set it to null
		}

		return ResponseEntity.ok(financialData);
	}












	//creating a criteria for specifying by quarters as well as a filter-->
	@GetMapping("/quarterByCriteria")
	public ResponseEntity<List<RevenueBudgetSummary>> getFinancialDataByCriteriaofQuarter(@RequestBody RevenueDTO criteria1) {
		// Check if any field has been provided in the request
		if (criteria1.isEmpty()) {
			// If no fields provided, return bad request
			return ResponseEntity.badRequest().build();
		}

		List<RevenueBudgetSummary> financialData1 = null;

		// Fetch data only if all criteria are provided and match
		if (areAllCriteriaMatchedforQuarter(criteria1)) {
			financialData1 = fetchData(criteria1);
		} else {
			// Indicate that the specified data doesn't exist
			financialData1 = Collections.emptyList(); // Or you can set it to null
		}

		return ResponseEntity.ok(financialData1);
	}

	private boolean areAllCriteriaMatchedforQuarter(RevenueDTO criteria1) {
		// Check if all criteria are provided and match
		return criteria1.getFinancialYear() != null &&
				criteria1.getProjectList() != null &&
				criteria1.getVerticalList() != null &&
				criteria1.getClassificationList() != null &&
				criteria1.getDmList() != null &&
				criteria1.getAccountList() != null &&
				criteria1.getPmList() != null &&
				criteria1.getQuarterList() != null;
	}


	private List<RevenueBudgetSummary> fetchData(RevenueDTO criteria) {
		// Fetch data from the repository based on the provided criteria
		return revenueRepo.findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerIn(
				criteria.getFinancialYear(),
				criteria.getProjectList(),
				criteria.getVerticalList(),
				criteria.getClassificationList(),
				criteria.getDmList(),
				criteria.getAccountList(),
				criteria.getPmList()
		);
	}


	@GetMapping("/dm/{deliveryManager}")
	public List<RevenueBudgetSummary> getRevenuesByDM(@PathVariable("deliveryManager") String deliveryManager) {
		List<RevenueBudgetSummary> revenues = revenueRepo.findBydeliveryManager(deliveryManager);
		return revenues;
	}

	@GetMapping("/quarter/{quarter}")
	public List<RevenueBudgetSummary> getRevenuesByquarter(@PathVariable("quarter") String quarter) {
		List<RevenueBudgetSummary> revenues = revenueRepo.findByquarter(quarter);
		return revenues;
	}

	@GetMapping("/month/{month}")
	public List<RevenueBudgetSummary> getRevenuesByMonth(@PathVariable("month") int month) {
		// Assuming month corresponds to financial year
		List<RevenueBudgetSummary> revenues = revenueRepo.findByFinancialYear(month);
		return revenues;
	}

	@GetMapping("/account/{account}")
	public List getRevenuesByAccount(@PathVariable("account") String account) {
		List revenues = revenueRepo.findByAccount(account);
		return revenues;
	}

	@GetMapping("/financialYear/{year}")
	public List getByFinancialYear(@PathVariable("year") int year) {
		return revenueRepo.findByFinancialYear(year);
	}
	@GetMapping("/vertical/{vertical}")
	public List<RevenueBudgetSummary> getByVertical(@PathVariable("vertical") String vertical) {
		return revenueRepo.findAllByVertical(vertical);
	}

	@GetMapping("/projectManager/{projectManager}")
	public List<RevenueBudgetSummary> getProjectManagerByName(@PathVariable("projectManager") String projectManager) {
		return revenueRepo.findByProjectManager(projectManager);
	}

	@GetMapping("/classification/{classification}")
	public List<RevenueBudgetSummary> getByClassification(@PathVariable String classification) {
		return revenueRepo.findAllByClassification(classification);
	}

	@GetMapping("/projectName/{projectName}")
	public List<RevenueBudgetSummary> getRevenueByProjectName(@PathVariable("projectName") String projectName) {
		return revenueRepo.findByProjectName(projectName);
	}

}