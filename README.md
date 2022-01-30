# Princeton WordNet for Java

This library wraps a Java API around the Princeton University WordNet database. Refer to 
https://wordnet.princeton.edu/ for the details regarding WordNet.

This is intended to be a third-party supporting library to any application that requires an 
english word dictionary. It provides a simple API for retrieving a word, its definition, and all 
synonyms and related words associated with the word.

This implementation supplements the data from the synonym WordNet data set with additional 
synonyms collected from other online sources. Along with that, an additional data set containing
related words to each word from WordNet is also compiled. 

This project was built using the Spring Boot library and exposes a single Spring Bean, called 
WordDictionary, which can be injected in to any application built using Spring. 

This project makes no HTTP calls to any online sources. All words in the dictionary are included 
in this project and are loaded into memory upon initial access to the WordDictionary implementation.

## Code Examples

```java
// @Service tells Spring that your class is a Spring Bean
@Service
public class YourSpringClass {

    // inject an implementation into your class using Spring's dependency injection framework
    @Resource
    private WordDictionary wordDictionary;
    
    public void demo() {
        // look up a word in the dictionary.
        Word word = wordDictionary.lookup("show");
        if (word == null) {
            // not found in the dictionary
            return;
        }

        // multiple definitions can exist for the word
        List<Definition> definitions = word.getDefinitions();
        for (Definition definition : definitions) {
            // possible definition types are NOUN, VERB, ADJECTIVE, ADVERB
            if (definition.getDefinitionType() == DefinitionType.VERB) {
                System.out.println("The definition of 'show' as verb is ...");
                System.out.println(definition.getDefinition());
            }
        }
        
        // retrieve the synonyms for a word, grouped by their definition type
        // supply true to the second argument to include synonyms from additional
        //   sources other than WordNet; false just to use the WordNet data sets.
        Map<DefinitionType, Set<String>> synonyms = wordDictionary.getSynonyms("show", false);
        if (synonyms == null) {
            System.out.println("No synonyms");
        } else {
            // just the synonyms that are nouns
            Set<String> nouns = synonyms.get(DefinitionType.NOUN);
            // just the synonyms that are verbs
            Set<String> verbs = synonyms.get(DefinitionType.VERB);
            // etc.
        }

        // retrieve only the noun and verb synonyms, grouped by their definition type, 
        // using both the WordNet data sets AND the additional sources.
        Map<DefinitionType, Set<String>> nounVerbsOnly = 
                wordDictionary.getSynonyms("show", true, 
                        DefinitionType.NOUN, DefinitionType.VERB);

        // retrieves all synonyms for your word and only uses the WordNet data sets
        Set<String> allSynonyms = wordDictionary.getAllSynonyms("show", false);

        // retrieves synonyms for your word, including from additional sources, and 
        //  only returns the nouns and verbs
        Set<String> allSynonyms = wordDictionary.getAllSynonyms("show", true,
                DefinitionType.NOUN, DefinitionType.VERB);
        
        // retrieve any related words associated with your word
        Set<String> related = wordDictionary.getAllRelated("show");
        
        // retrieve any related words to your word, but just the verbs and adverbs
        Set<String> related = wordDictionary.getAllRelated("show", 
                DefinitionType.VERB, DefinitionType.ADVERB);
    }
}
```

The WordDictionary implementation does not have any dependencies. Because of that, it is not a
requirement that your application uses Spring Boot. You can simply instantiate
the WordDictionaryImpl class and call the desired methods.

```java
WordDictionary wordDictionary = new WordDictionaryImpl();

// call the methods using the wordDictionary instance
Word word = wordDictionary.lookup("earth");
```


## Maven or Gradle

You can include this project using Gradle or Maven.

### Gradle (Short)
```text
    repositories {
        mavenCentral()
    }

    implementation 'com.estepsoftwareforensics:princeton-wordnet-java:1.0.0'
```
### Gradle
```text
    repositories {
        mavenCentral()
    }

    implementation group: 'com.estepsoftwareforensics', name: 'princeton-wordnet-java', version: '1.0.0'
```

### Maven POM
```xml
<dependency>
    <groupId>com.estepsoftwareforensics</groupId>
    <artifactId>princeton-wordnet-java</artifactId>
    <version>1.0.0</version>
</dependency>
```