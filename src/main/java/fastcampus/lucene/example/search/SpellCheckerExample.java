package fastcampus.lucene.example.search;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/*
  #A 스펠체커용 인덱스 생성
  #B 편집거리 계산
  #C 후보군 생성
*/
public class SpellCheckerExample {


  public static void main(String[] args) throws Exception {
    Directory directory = FSDirectory.open(Paths.get("./index/spell/"));

    SpellChecker spellChecker = new SpellChecker(directory);
    Analyzer analyzer = new StandardAnalyzer();                             //기본 스탠다드분석기를 사용함
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);  //인덱스 Writer의 설정을 지정하는 클래스

    Path path = Paths.get("./data/spell/dic.txt");

    spellChecker.setSpellIndex(directory);
    spellChecker.indexDictionary(
            new PlainTextDictionary(path),indexWriterConfig,true);
    String wordForSuggestions = "삼성전";
    spellChecker.setStringDistance(new LevensteinDistance());  //#Levenstein 편집거리 알고리즘
//    //spell.setStringDistance(new JaroWinklerDistance());  //Jaro-Winkler 알고리즘

    int suggestionsNumber = 5;
    String[] suggestions = spellChecker.
            suggestSimilar(wordForSuggestions, suggestionsNumber);
    if (suggestions != null && suggestions.length > 0) {

      for (String word : suggestions) {

        System.out.println("Did you mean:" + word);

      }

    } else {

      System.out.println("No suggestions found for word:" + wordForSuggestions);

    }


  }

}


