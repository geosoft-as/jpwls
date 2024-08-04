## OSDU Practical Well Log Standard (PWLS)

The [OSDU](https://osduforum.org/OSDU) _Practical Well Log Standard_ (PWLS) provides an industry-agreed list of logging tool
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
is provided by GeoSoft and is availble here. The jpwls repository contains
an _implementation_ of this standard.


## Using jpwls as a web secrvice

jpwls is a REST API web service that can run locally or in cloud.
A Proof of Concept implementation is hosted on AWS.
Any software can access up-to-date PWLS information using simple
REST calls like:

`https://127.0.0.1/companies` ([Try it!](https://127.0.0.1/companies)

`https://127.0.0.1/tools` ([Try it!](https://127.0.0.1/companies)

`https://127.0.0.1/tools?company=440` ([Try it!](https://127.0.0.1/companies?company=440)

and so on.

Full REST API documentation is availble here.


## Using jpwls as a Java library

If the client software is already using Java, jpwls can be embedded
directly to provide the PWLS information as Java [POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object)s
instead of going through the JSON streams.

The `Pwls` class provides the API towards PWLS and it can be used like this:

```Java
   Pwls pwls = new Pwls("https://raw.githubusercontent.com/geosoft-as/pwls/main/json");
   Set<Companie> companies = pwls.getCompanies();
   Set<Tool> tools = pwls.getTools();
   ...
```

Note that even if the library is embedded in your application, the information is
still taken from the dynamic GitHub PWLS source and will always be up to date,
even (depending on implementation) without restarting your application.


## Contact

For inqueries on the GeoSoft PWLS implementation please contact
[info@geosoft.no](mailto:info@geosoft.no)

