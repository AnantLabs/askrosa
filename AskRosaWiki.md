# Introduction #

AskRosa FTP search engine is a general-purpose FTP search engine based Apache Lucene. In this project, we implement various functions, e.g. search suggestion, search history statistics, advanced search and FTP sites management .etc. The development environment is MyEclipse+Tomcat+MySQL。 We employ struts and jsp to code the web-front and all the basic search engine functions like crawler and indexer are implemented with pure java code.

We also put some time to make the project easy to deploy. If you want to have a local FTP search engine, like in a college campus or in a company's local area network or an internet wide FTP search engine, AskRosa will be one of your choices.


# Details #
Modules:
Crawler: fetch files information from various FTP Servers

Indexer: indexing all the information fetched by crawler module

WebFTP:  download resources from all available servers concurrently, if a file exists in lots of servers, this tool will download this file from these servers by cut the file into slices.

Auto Search Suggestion: offer the users keyword suggestions according to their current input

Proxy: this module help the crawler to crawl some sites through a proxy, this function is very useful in some campuses as the limitation of their network access.

Web-front: provide the search interface to all internet users