# jpwls

jpwls is an implementation of the [OSDU](https://osduforum.org/OSDU) PWLS standard,
either as a _REST API web service_ or a _Java library_.


## OSDU Practical Well Log Standard (PWLS)

The OSDU _Practical Well Log Standard_ (PWLS) provides an industry-agreed list of logging tool
classes and a hierarchy of measurement properties and applies all known log mnemonics to them.
When this information is implemented in stores or software, it supports queries over large
populations of log data, making it easier for oil and gas professionals to find and use this
valuable data.

PWLS exists in the following versions:

* [v3.0](https://energistics.org/practical-well-log-standard) (February 25, 2021)
* [v2.0](https://energistics.org/sites/default/files/2023-03/pwls_20.htm) (September 30, 2003)
* [v1.0](https://energistics.org/sites/default/files/2023-03/pwls_10.htm) (February 27, 2001)

These are defined by static MS/Excel spreadsheets.

A dynamic version of the standard based on the JSON format
is provided by GeoSoft and is availble in the [pwls](https://github.com/geosoft-as/pwls) repository.

The jpwls repository contains a Java implementation of this standard.


## Using jpwls as a web service

jpwls is a REST API web service that can run locally or in cloud.
A Proof of Concept cloud implementation is hosted in Amazon Web Services (AWS) @ [http://127.0.0.1:8000](http://127.0.0.1:8000).
Any software can access up-to-date PWLS information by simple REST calls like:

* `http://13.60.27.155/companies`          ([Try it!](http://13.60.27.155/companies))
* `http://13.60.27.155/tools`              ([Try it!](http://13.60.27.155/tools))
* `http://13.60.27.155?company=440`        ([Try it!](http://13.60.27.155/companies?code=440))

and so on.

Full REST API documentation is availble here (soon).


## Using jpwls as a Java library

If the client software is already using Java, jpwls can be embedded
directly to provide the PWLS information as Java [POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object)s
instead of going through JSON streams.

The `Pwls` class provides the API towards PWLS and it can be used like this:

```Java
   :
   using no.geosoft.jpwls.Pwls;
   using no.geosoft.jpwls.Company;
   using no.geosoft.jpwls.Tool;
   :


   // Establish link to the PWLS source
   Pwls pwls = new Pwls("https://raw.githubusercontent.com/geosoft-as/pwls/main/json");

   // Get all companies defined by the PWLS
   Set<Company> companies = pwls.getCompanies();

   // Get all tools defined by the PWSL
   Set<Tool> tools = pwls.getTools();

   // etc
```

Full Java API documentation is available [here](https://htmlpreview.github.io/?https://raw.githubusercontent.com/geosoft-as/jpwls/main/docs/index.html).

Note that even if the library is embedded in an application, the information is
still taken from the dynamic GitHub PWLS source and will always be up to date,
showing live changes potentially without restarting the application.


## Contact

For inqueries on the GeoSoft PWLS implementation please contact
[info@geosoft.no](mailto:info@geosoft.no)


