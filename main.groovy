import csv.CsvParser
import csv.CsvSection

//String uri = "C:/Files/Results_0008_100m Men25_A2.csv"
String uri = "C:/Files/Starters_0008_100m Men25_A2.csv"
CsvSection eventSection = new CsvSection([name: "Description", length: 1])
CsvSection detailSection = new CsvSection([name: "Details", length: 0])

CsvParser parser = new CsvParser(uri, eventSection, detailSection)

println parser.convertToXml()