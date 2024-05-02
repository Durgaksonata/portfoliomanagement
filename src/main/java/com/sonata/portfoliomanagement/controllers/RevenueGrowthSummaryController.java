package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;

import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("revenuegrowths")
public class RevenueGrowthSummaryController {

    public RevenueGrowthSummaryController(RevenueGrowthSummaryRepository revenuegrowthRepo) {
        this.revenuegrowthRepo = revenuegrowthRepo;
    }

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    RevenueGrowthSummaryRepository revenuegrowthRepo;



    @PostMapping("/save")
    public ResponseEntity<RevenueGrowthSummary> createRevenueGrowthSummary(@RequestBody RevenueGrowthSummary revenue) {
        RevenueGrowthSummary createdRevenue = revenuegrowthRepo.save(revenue);
        return new ResponseEntity<>(createdRevenue, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<RevenueGrowthSummary>> getAllRevenueBudgets() {
        List<RevenueGrowthSummary> allRevenueBudgets = revenuegrowthRepo.findAll();
        return ResponseEntity.ok(allRevenueBudgets);
    }

    @GetMapping("/getverticallist")
    public List<String> getVerticalList()
    {
        List<String> verticalList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();
        for(RevenueGrowthSummary request : revenueData ) {
            verticalList.add(request.getVertical());
        }
        return verticalList.stream().distinct().collect(Collectors.toList());
    }
    @PostMapping("/vertical/post")
    public List<String> stringList2(@RequestBody RevenueDTO verticalList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList2 = verticalList.getMyList();
        return stringList2;
    }

    @GetMapping("/verticals")
    public List<RevenueGrowthSummary> getRevenueByVertical(@RequestBody RevenueDTO verticalList) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/vertical/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, verticalList, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList2 = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findByVertical(getList);
            resultList2.addAll(filteredList);
        }

        return resultList2;
    }


    @GetMapping("/vertical/{vertical}")
    public List<RevenueGrowthSummary> getByVertical(@PathVariable("vertical") String vertical) {
        return revenuegrowthRepo.findAllByVertical(vertical);
    }
    @GetMapping("/getclassificationlist")
    public List<String> getClassificationList() {
        List<String> classficationList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();

        for (RevenueGrowthSummary request : revenueData ) {
            classficationList.add(request.getClassification());
        }
        return classficationList.stream().distinct().collect(Collectors.toList());
    }
    @PostMapping("/classification/post")
    public List<String> stringList1(@RequestBody RevenueDTO classificationList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList1 = classificationList.getMyList();
        return stringList1;
    }

    @GetMapping("/classifications")
    public List<RevenueGrowthSummary> getRevenuebyClassification(@RequestBody RevenueDTO classificationList) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/classification/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, classificationList, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList1 = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findByclassification(getList);
            resultList1.addAll(filteredList);
        }

        return resultList1;
    }


    @GetMapping("/classification/{classification}")
    public List<RevenueGrowthSummary> getByClassification(@PathVariable("classification") String classification) {
        return revenuegrowthRepo.findAllByClassification(classification);
    }

    @GetMapping("/getdmlist")
    public List<String> getdeliveryManagers()
    {
        List<String> dmList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();

        for (RevenueGrowthSummary request : revenueData ) {
            dmList.add(request.getDeliveryManager());
        }
        return dmList.stream().distinct().collect(Collectors.toList());
    }
    //This POST method puts the specific DMS required to get data from.Multiple DMs sent through the GET method below
    @PostMapping("/DM/post")
    public List<String> stringList(@RequestBody RevenueDTO dmList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList = dmList.getMyList();
        return stringList;
    }

    //The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple DMS
    @GetMapping("/DMs")
    public List<RevenueGrowthSummary> getRevenuebyDM(@RequestBody RevenueDTO dmList) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuegrowths/DM/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, dmList, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findBydeliveryManager(getList);
            resultList.addAll(filteredList);
        }

        return resultList;
    }

    @GetMapping("/dm/{deliveryManager}")
    public List<RevenueGrowthSummary> getRevenuesByDM(@PathVariable("deliveryManager") String deliveryManager) {
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findBydeliveryManager(deliveryManager);
        return revenues;
    }
    @GetMapping("/getaccountlist")
    public List<String> getAccounts ()
    {
        List<String> accountList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();
        for(RevenueGrowthSummary request : revenueData ) {
            accountList.add(request.getAccount());
        }
        return accountList.stream().distinct().collect(Collectors.toList());
    }

    //This POST method puts the specific accounts required to get data from.Multiple accounts sent through the GET method below
    @PostMapping("/account/post")
    public List<String> stringListForAccount(@RequestBody RevenueDTO accountList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList = accountList.getMyList();
        return stringList;
    }

    //The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple accounts
    @GetMapping("/Accounts")
    public List<RevenueGrowthSummary> getRevenuebyAccount(@RequestBody RevenueDTO accountList) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/account/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, accountList, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findByaccount(getList);
            resultList.addAll(filteredList);
        }

        return resultList;
    }
    @GetMapping("/account/{account}")
    public List<RevenueGrowthSummary> getbyaccount(@PathVariable String account) {
        return revenuegrowthRepo.findByAccount(account);
    }


    @GetMapping("/getpmlist")
    public List<String> getProjectManager ()
    {
        List<String> pmList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();
        for(RevenueGrowthSummary request : revenueData ) {
            pmList.add(request.getProjectManager());
        }
        return pmList.stream().distinct().collect(Collectors.toList());
    }

    @PostMapping("/pm/post")
    public List<String> pmstringList(@RequestBody RevenueDTO pmlist) {
        // Extract the list of strings from the RevenueDTO object
        List<String> pmstringList = pmlist.getMyList();
        return pmstringList;
    }

    //The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple DMS
    @GetMapping("/pms")
    public List<RevenueGrowthSummary> getRevenuebyPM(@RequestBody RevenueDTO pmlist) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/pm/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, pmlist, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList =revenuegrowthRepo.findByprojectManager(getList);
            resultList.addAll(filteredList);
        }

        return resultList;
    }


    @GetMapping("projectManager/{projectManager}")
    public List<RevenueGrowthSummary> getbypm(@PathVariable String projectManager) {
        return revenuegrowthRepo.findByProjectManager( projectManager);
    }

    @GetMapping("/getprojectlist")
    public List<String> getProjectList()
    {
        List<String> projectList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();
        for(RevenueGrowthSummary request : revenueData ) {
            projectList.add(request.getProjectName());
        }
        return projectList.stream().distinct().collect(Collectors.toList());
    }

    @PostMapping("/project/post")
    public List<String> stringList3(@RequestBody RevenueDTO projectList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList3 = projectList.getMyList();
        return stringList3;
    }

    @GetMapping("/projects")
    public List<RevenueGrowthSummary> getRevenueByProject(@RequestBody RevenueDTO projectList) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/project/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, projectList, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList3 = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findByProjectName(getList);
            resultList3.addAll(filteredList);
        }

        return resultList3;
    }


    @GetMapping("projectName/{projectName}")
    public List<RevenueGrowthSummary> getbyprojectName(@PathVariable String project) {
        return revenuegrowthRepo.findByProjectName( project);
    }

    @GetMapping("/getfinancialyearlist")
    public List<Integer> getFinancialYearList() {
        List<Integer> fyList = new ArrayList<>();
        List<RevenueGrowthSummary> revenueData = revenuegrowthRepo.findAll();
        for (RevenueGrowthSummary request : revenueData) {
            fyList.add(request.getFinancialYear());
        }
        return fyList.stream().distinct().collect(Collectors.toList());
    }


    //This POST method puts the specific FYs required to get data from.Multiple Fys sent through the GET method below
    @PostMapping("/FY/post")
    public List<Integer> fyIntegerList(@RequestBody RevenueDTO yearsList) {
        List<Integer> integerList = yearsList.getGetanotherList();
        return integerList;
    }

    @GetMapping("/FYs")
    public List<RevenueGrowthSummary> getRevenuebyFY(@RequestBody RevenueDTO yearsList) {
        String postUrl = "http://localhost:8081/revenuebudgets/FY/post";
        List<Integer> postResponse = restTemplate.postForObject(postUrl, yearsList, List.class);
        List<RevenueGrowthSummary> resultList = new ArrayList<>();
        for (Integer year : postResponse) {
            List<RevenueGrowthSummary> filteredList = revenuegrowthRepo.findByFinancialYear(year);
            resultList.addAll(filteredList);
        }
        return resultList;
    }

    @GetMapping("/financialYear/{year}")
    public List getByFinancialYear(@PathVariable("year") int year) {
        return revenuegrowthRepo.findByFinancialYear(year);
    }

    @GetMapping("/getquarterlist")
    public List<String> getQuarterList()
    {
        List<String> quarterList = new ArrayList<String>();
        List<RevenueGrowthSummary> revenueData= revenuegrowthRepo.findAll();
        for(RevenueGrowthSummary request : revenueData ) {
            quarterList.add(request.getQuarter());
        }
        return quarterList.stream().distinct().collect(Collectors.toList());
    }

    //This POST method puts the specific quarters required to get data from.Multiple quarters sent through the GET method below
    @PostMapping("/quarter/post")
    public List<String> stringListForQuarter(@RequestBody RevenueDTO quarterList) {
        // Extract the list of strings from the RevenueDTO object
        List<String> stringList = quarterList.getMyList();
        return stringList;
    }

    //The GET method calls the POST method above and need to send the request body for this get method to obtain for Multiple quartersS
    @GetMapping("/quarters")
    public List<RevenueGrowthSummary> getRevenuebyquarter(@RequestBody RevenueDTO quarterlist) {
        // Call the POST endpoint to get the list of strings
        String postUrl = "http://localhost:8081/revenuebudgets/quarter/post";
        List<String> postResponse = restTemplate.postForObject(postUrl, quarterlist, List.class);

        // Create a list to store Revenue objects
        List<RevenueGrowthSummary> resultList = new ArrayList<>();

        // Iterate over the response strings and filter Revenue objects accordingly
        for (String getList : postResponse) {
            List<RevenueGrowthSummary> filteredList =revenuegrowthRepo.findByquarter(getList);
            resultList.addAll(filteredList);
        }

        return resultList;
    }

    @GetMapping("/quarter/{quarter}")
    public List<RevenueGrowthSummary> getRevenuesByquarter(@PathVariable("quarter") String quarter) {
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByquarter(quarter);
        return revenues;
    }

    @GetMapping("/getmonthlist")
    public List<Date> getMonth() {
        List<Date> monthList = new ArrayList<>();
        List<RevenueGrowthSummary> revenueData = revenuegrowthRepo.findAll();
        for (RevenueGrowthSummary request : revenueData) {
            monthList.add(request.getMonth());
        }
        return monthList.stream().distinct().collect(Collectors.toList());
    }
    @GetMapping("/month/{month}")
    public List<RevenueGrowthSummary> getRevenuesByMonth(@PathVariable("month") int month) {
        // Assuming month corresponds to financial year
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByFinancialYear(month);
        return revenues;
    }


    @PostMapping("/accountbydm")
    public ResponseEntity<Set<String>> getAccountByDM(@RequestBody RevenueDTO dmList) {
        List<String> dmNames = dmList.getMyList();
        Set<String> account = new HashSet<>();

        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByDeliveryManagerIn(dmNames);

        for (RevenueGrowthSummary revenue : revenues) {
            account.add(revenue.getAccount());
        }
        return ResponseEntity.ok(account);
    }
    @PostMapping("/projectbypm")
    public ResponseEntity<Set<String>> getprojectBypM(@RequestBody RevenueDTO pmList) {
        List<String> pmNames = pmList.getMyList();
        Set<String> project = new HashSet<>();

        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByProjectManagerIn(pmNames);
        for (RevenueGrowthSummary revenue : revenues) {
            project.add(revenue.getProjectName());
        }
        return ResponseEntity.ok(project);
    }
    @PostMapping("/pmbyaccount")
    public ResponseEntity<Set<String>> getpmByacnt(@RequestBody RevenueDTO pmList){
        List<String>accountNames = pmList.getMyList();
        Set<String>  projectManager= new HashSet<>();


        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByAccountIn(accountNames);
        for (RevenueGrowthSummary revenue : revenues) {
            projectManager.add(revenue.getProjectManager());
        }

        return ResponseEntity.ok(projectManager);
    }

    @PostMapping("/dmbyclassification")
    public ResponseEntity<Set<String>> getDMByClassification(@RequestBody RevenueDTO classificationList) {
        List<String> classificationNames = classificationList.getMyList();
        Set<String> dmNames = new HashSet<>();
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByClassificationIn(classificationNames); // Change method name
        for (RevenueGrowthSummary revenue : revenues) {
            dmNames.add(revenue.getDeliveryManager());
        }
        return ResponseEntity.ok(dmNames);
    }

    @PostMapping("/dmbyvertical")
    public ResponseEntity<Set<String>> getDMByVertical(@RequestBody RevenueDTO verticalList) {
        List<String> verticals = verticalList.getMyList();
        Set<String> deliveryManagerNames = new HashSet<>();

        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByVerticalIn(verticals);
        for (RevenueGrowthSummary revenue : revenues) {
            deliveryManagerNames.add(revenue.getDeliveryManager());
        }

        return ResponseEntity.ok(deliveryManagerNames);
    }


    @PostMapping("/classificationbyvertical")
    public ResponseEntity<Set<String>> getClassificationByVertical(@RequestBody RevenueDTO verticalList) {
        List<String> VerticalNames = verticalList.getMyList();
        Set<String> classificationNames = new HashSet<>();
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByVerticalIn(VerticalNames); // Change method name
        for (RevenueGrowthSummary revenue : revenues) {
            classificationNames.add(revenue.getClassification());
        }
        return ResponseEntity.ok(classificationNames);
    }


    @PostMapping("/dmbyvertical&classification")
    public ResponseEntity<Set<String>> getDMByVerticalAndClassification(@RequestBody RevenueDTO dmList) {
        List<String> verticals = dmList.getVerticalList();
        List<String> classifications = dmList.getClassificationList();
        Set<String> deliveryManagerNames = new HashSet<>();

        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByVerticalInAndClassificationIn(verticals, classifications);
        for (RevenueGrowthSummary revenue : revenues) {
            deliveryManagerNames.add(revenue.getDeliveryManager());
        }
        return ResponseEntity.ok(deliveryManagerNames);
    }



    @PostMapping("/accountbydmclassification")
    public ResponseEntity<Set<String>> getAccountByDMAndClassification(@RequestBody RevenueDTO dmClassificationList) {
        List<String> dmNames = dmClassificationList.getDMList();
        List<String> classifications = dmClassificationList.getClassificationList();
        Set<String> accounts = new HashSet<>();

        // Retrieve revenues based on both DMs and classifications
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByDeliveryManagerInAndClassificationIn(dmNames, classifications);
        // Extract account names from the retrieved revenues
        for (RevenueGrowthSummary revenue : revenues) {
            accounts.add(revenue.getAccount());
        }

        return ResponseEntity.ok(accounts);
    }


    @PostMapping("/pmbyaccountclassification")
    public ResponseEntity<Set<String>> getPMByAccountAndClassification(@RequestBody RevenueDTO pmAccountClassificationList) {
        List<String> accountNames = pmAccountClassificationList.getMyList();
        List<String> classifications = pmAccountClassificationList.getClassificationList();
        Set<String> projectManagers = new HashSet<>();

        // Retrieve revenues based on both account names and classifications
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByAccountInAndClassificationIn(accountNames, classifications);

        // Extract project managers from the retrieved revenues
        for (RevenueGrowthSummary revenue : revenues) {
            projectManagers.add(revenue.getProjectManager());
        }

        return ResponseEntity.ok(projectManagers);
    }


    @PostMapping("/projectbypmclassification")
    public ResponseEntity<Set<String>> getProjectsByPMAndClassification(@RequestBody RevenueDTO pmClassificationList) {
        List<String> pmNames = pmClassificationList.getPmList();
        List<String> classifications = pmClassificationList.getClassificationList();
        Set<String> projects = new HashSet<>();

        // Retrieve revenues based on both PMs and classifications
        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByProjectManagerInAndClassificationIn(pmNames, classifications);
        // Extract project names from the retrieved revenues
        for (RevenueGrowthSummary revenue : revenues) {
            projects.add(revenue.getProjectName());
        }

        return ResponseEntity.ok(projects);
    }

    @PostMapping("/financialYearByProject")
    public ResponseEntity<Set<Integer>> getFinancialYearByProject(@RequestBody RevenueDTO projectList) {
        List<String> projectNames = projectList.getMyList();
        Set<Integer> financialYears = new HashSet<>();

        List<RevenueGrowthSummary> revenues = revenuegrowthRepo.findByProjectNameIn(projectNames);
        for (RevenueGrowthSummary revenue : revenues) {
            financialYears.add(revenue.getFinancialYear());
        }
        return ResponseEntity.ok(financialYears);
    }


    //using this get method to get the data by using six filters.Only if a matching data for the specified request body exists in the database, it'll fetch that data or else it provides null
    @PostMapping("/financialYearByCriteria")
    public ResponseEntity<List<RevenueGrowthSummary>> getFinancialDataByCriteria(@RequestBody RevenueDTO criteria) {
        // Check if any field has been provided in the request
        if (criteria.isEmpty()) {
            // If no fields provided, return bad request
            return ResponseEntity.badRequest().build();
        }
        List<RevenueGrowthSummary> financialData = null;
        // Fetch data only if all criteria are provided and match
        if (areAllCriteriaMatched(criteria)) {
            financialData = fetchData(criteria);
        } else {
            // Indicate that the specified data doesn't exist
            financialData = Collections.emptyList(); // Or you can set it to null
        }
        return ResponseEntity.ok(financialData);
    }

    private List<RevenueGrowthSummary> fetchData(RevenueDTO criteria) {
        // Fetch data from the repository based on the provided criteria
        return revenuegrowthRepo.findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(
                criteria.getFinancialYear(),
                criteria.getProjectList(),
                criteria.getVerticalList(),
                criteria.getClassificationList(),
                criteria.getDmList(),
                criteria.getAccountList(),
                criteria.getPmList(),
                criteria.getQuarterList());

    }
    private boolean areAllCriteriaMatched(RevenueDTO criteria) {
        // Check if all criteria are provided and match
        return criteria.getFinancialYear() != null &&
                criteria.getProjectList() != null &&
                criteria.getVerticalList() != null &&
                criteria.getClassificationList() != null &&
                criteria.getDmList() != null &&
                criteria.getAccountList() != null &&
                criteria.getPmList() != null;
    }


    //This method takes quarter(Q1,Q2,Q3,Q4) and gives me their respective financial year's months-->
    @PostMapping("/monthsByQuarter")
    public ResponseEntity<Set<String>> getMonthsByQuarter(@RequestBody RevenueDTO quarterList) {
        List<String> quarters = quarterList.getMyList();
        Set<String> months = new HashSet<>();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);

        for (String quarter : quarters) {
            switch (quarter) {
                case "Q1":
                    months.addAll(getMonthsForQuarter(4, monthFormat));
                    break;
                case "Q2":
                    months.addAll(getMonthsForQuarter(7, monthFormat));
                    break;
                case "Q3":
                    months.addAll(getMonthsForQuarter(10, monthFormat));
                    break;
                case "Q4":
                    months.addAll(getMonthsForQuarter(1, monthFormat));
                    break;
                default:
                    System.out.println("Enter Valid Data!");
                    break;
            }
        }

        return ResponseEntity.ok(months);
    }

    private List<String> getMonthsForQuarter(int startMonth, SimpleDateFormat monthFormat) {
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, startMonth - 1); // Month in Java Calendar starts from 0

        for (int i = 0; i < 3; i++) {
            months.add(monthFormat.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }

        return months;
    }



    @PostMapping("/databyverticalandclassification")
    public ResponseEntity<Set<Map<String, Object>>> getDataByVerticalAndClassification(@RequestBody RevenueDTO requestDTO) {
        // Check if all criteria are provided
        if (!areAllCriteriaProvided(requestDTO)) {
            // If any criteria are missing, return bad request
            return ResponseEntity.badRequest().build();
        }

        List<String> verticals = requestDTO.getVerticalList();
        List<String> classifications = requestDTO.getClassificationList();
        Set<Map<String, Object>> data = new HashSet<>();

        // Retrieve data based on both verticals and classifications
        List<RevenueGrowthSummary> dataList = revenuegrowthRepo.findByVerticalInAndClassificationIn(verticals, classifications);

        // Check if data is retrieved correctly
        System.out.println("Data Retrieved: " + dataList);

        // Extract data fields from the retrieved data
        for (RevenueGrowthSummary dataItem : dataList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", dataItem.getId());
            dataMap.put("vertical", dataItem.getVertical());
            dataMap.put("classification", dataItem.getClassification());
            dataMap.put("deliveryDirector", dataItem.getDeliveryDirector());
            dataMap.put("deliveryManager", dataItem.getDeliveryManager());
            dataMap.put("account", dataItem.getAccount());
            dataMap.put("projectManager", dataItem.getProjectManager());
            dataMap.put("projectName", dataItem.getProjectName());
            dataMap.put("financialYear", dataItem.getFinancialYear());
            dataMap.put("quarter", dataItem.getQuarter());
            dataMap.put("month", dataItem.getMonth());
            dataMap.put("AccountExpected", dataItem.getAccountExpected());
            dataMap.put("forecast", dataItem.getForecast());
            dataMap.put("gap", dataItem.getGap());


            data.add(dataMap);
        }

        return ResponseEntity.ok(data);
    }

    private boolean areAllCriteriaProvided(RevenueDTO requestDTO) {
        // Check if all criteria are provided
        return requestDTO.getVerticalList() != null && !requestDTO.getVerticalList().isEmpty()
                && requestDTO.getClassificationList() != null && !requestDTO.getClassificationList().isEmpty();
    }


}
