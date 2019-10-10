package csv

import groovy.xml.MarkupBuilder

class CsvParser {

    List<CsvSection> csvSections
    String uri

    private String separatorChar = ","
    private String linebreakChars = "\\r?\\n"
    private int valueWrapperLength = 1 //to get rid of those annoying double quotes

    CsvParser(String uri, CsvSection... csvSections){
        this.uri = uri
        this.csvSections = csvSections
    }

    String convertToXml(){

        List<List<String>> lines = []
        new File(this.uri).text.split(linebreakChars).each {
            lines.add(it.split(separatorChar).collect {it[valueWrapperLength..-valueWrapperLength*2]})
        }

        this.csvSections.each { CsvSection section ->
            section.headers = lines.get(0)
            lines = lines.drop(1)
            if (section.length > 0) {
                section.lines = lines[0..section.length - 1].collect()
                lines = lines.drop(section.length)
            }else{
                section.lines = lines.collect()
            }
        }


        this.csvSections.each {
            println it.name
            println it.headers
        }


        def stringWriter = new StringWriter()
        def peopleBuilder = new MarkupBuilder(stringWriter)
        peopleBuilder.Event {
            this.csvSections.each { section ->
                "${section.name}" {
                    if (section.lines.size() > 1) {
                        section.lines.each { List<String> l ->
                            Item {
                                section.headers.eachWithIndex { String header, int x ->
                                    "${header}"(l[x])
                                }
                            }
                        }
                    }else{
                        section.lines.each { List<String> l ->
                            section.headers.eachWithIndex { String header, int x ->
                                "${header}"(l[x])
                            }
                        }
                    }
                }
            }
        }
        return stringWriter.toString()
    }

}
