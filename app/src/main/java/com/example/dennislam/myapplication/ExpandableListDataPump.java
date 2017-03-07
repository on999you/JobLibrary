package com.example.dennislam.myapplication;

/**
 * Created by dennislam on 13/12/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> FAQ01 = new ArrayList<String>();
        FAQ01.add("SalaryCheck is an interactive search engine that helps you find and compare market-related salary information matched to your exact job profile on our daily updated database.");

        List<String> FAQ02 = new ArrayList<String>();
        FAQ02.add("The data contained in your search results have been collected from job seekers by means of a web-based survey, via regularly updated member profiles and salary information posted by employers as part of recruitment advertisements, as well as from salary reports provided by recruitment agencies. This enables us to produce statistically correct, individualised salary reports covering thousands of job titles.");

        List<String> FAQ03 = new ArrayList<String>();
        FAQ03.add("We do not offer any payment, special deals or other incentives to encourage participation in our salary surveys. The salary information in SalaryCheck is compiled using voluntarily collected data and thus, as is the case with other surveys of this kind, we cannot guarantee 100% accuracy. However, we urge respondents to provide only true and reliable personal and employment details, and SalaryCheck is updated daily and improved over time, aiming to provide the most reliable salary information possible. The data is also checked and benchmarked before calculation, in an attempt to eliminate any unreliable information.\n" +
                "\n" +
                "Integrity of data\n\n"  +
                "All salary information submitted by third parties are carefully filtered being added to our active database. We also have guidelines on confirming data validity before adding it to the database. We eliminate duplicate entries, and any 'unusual' profiles are professionally analysed before being allowed onto the database.\n" +
                "\n" +
                "Data analysis\n\n" +
                "We only report actual data that we have collected and approved, thereby avoiding erroneous inference assumptions. We do not blend multiple sources of data into a single search result.\n" +
                "- We only use profiles submitted within the last three years\n" +
                "- We do not modify or blend profile data\n" +
                "- We do not apply inflation or cost-of-living adjustments\n" +
                "- We do not estimate salaries using information from other regions\n" +
                "\n" +
                "Our algorithms analyse up to five comparable data points (eg job function, industry, position, and working experience) of individuals with similar attributes to generate each search result. The matches are precise and exceed traditional sample sizes for highly detailed comparisons of this nature. SalaryCheck is based on a minimum of one profile match (eg job function, industry, job title, or working experience). If we cannot find at least one profile match for a position, we consider the data insufficient to provide an accurate representation of the market and therefore cannot produce a report. ");

        List<String> FAQ04 = new ArrayList<String>();
        FAQ04.add("We adjust calculations for hours worked, but we do not include any bonuses in salary calculations.");

        List<String> FAQ05 = new ArrayList<String>();
        FAQ05.add("Not all employees with comparable working experience and education levels are on similar wage packages. If your salary is higher than that predicted by SalaryCheck, you may have some qualities, achievements or talents that have been rewarded by your employer, but that cannot be quantified by SalaryCheck. On the other hand, if your salary is lower than that predicted, you may simply work for a company that pays less but offers more benefits and/or a better working environment. However, it may also mean that you are underpaid - in which case, you might want to consider looking for another job.");

        List<String> FAQ06 = new ArrayList<String>();
        FAQ06.add("Firstly, refine your search (entering your job title, work experience, industry, and so forth). SalaryCheck currently recognises more than 10,000 different job titles. While it is an encompassing tool, your profession may not yet be defined if it is highly specialised or unusual. If you suspect that is the case, let us know so that we can include your search criteria in SalaryCheck.");

        List<String> FAQ07 = new ArrayList<String>();
        FAQ07.add("SalaryCheck aims to provide the most accurate information possible about expected salaries for employees in specific occupations and with particular characteristics and experience, but our calculations do not consider personal qualities such as confidence, work ethics and attitude, and creative thinking. These qualities are all contributing factors that help to determine your salary. SalaryCheck's predictions are therefore informative, but cannot be the chief factor during wage negotiations. On the other hand, there is nothing stopping you from referring to the salary levels enjoyed by your professional peers during wage discussions.");

        List<String> FAQ08 = new ArrayList<String>();
        FAQ08.add("The median is the central value in a set of data that has been arranged in order of size. It is the value above which and below which half of cases fall, which means half the population earns more, and half less, than a person earning the median salary. It is also called the 50th percentile.");

        //=========================================================================================================

        expandableListDetail.put("What is SalaryCheck? ", FAQ01);
        expandableListDetail.put("How does SalaryCheck collect the data? ", FAQ02);
        expandableListDetail.put("Is the salary information in SalaryCheck reliable? And how often is SalaryCheck updated? ", FAQ03);
        expandableListDetail.put("Are overtime and bonuses included? ", FAQ04);
        expandableListDetail.put("My salary differs widely from those shown in the Salary Check. How can that be?  ", FAQ05);
        expandableListDetail.put("My profession isn't listed. Is this possible?  ", FAQ06);
        expandableListDetail.put("Can I use the SalaryCheck results as a bargaining tool with my employer?  ", FAQ07);
        expandableListDetail.put("What is the median wage?  ", FAQ08);
        return expandableListDetail;
    }
}