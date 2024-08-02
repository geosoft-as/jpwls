ProjectDisplayName = PWLS

JavaPackages = \
        no/geosoft/pwls \
        no/geosoft/pwls/excel \
        no/geosoft/pwls/json \

JavadocPackages = -subpackages no

JavaLibraries = \
        poi-3.11-beta3-20141111.jar \
        poi-ooxml-3.11-beta3-20141111.jar \
        poi-ooxml-schemas-3.11-beta3-20141111.jar \
	xmlbeans-2.6.0.jar \
	javax.json-1.1.3.jar \
	javax.json-api-1.1.3.jar \
	justify-0.15.0.jar \

JavaMainClass = no.geosoft.pwls.PwlsWebService

include $(DEV_HOME)/tools/Make/Makefile



