/*
 * The MIT License
 *
 * Copyright 2018 nuno.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.unimaas.ids.data2services.service;

import java.util.ArrayList;
import java.util.List;

import nl.unimaas.ids.data2services.model.DataSource;

/**
 *
 * @author nuno
 */
public class DataSourceService {
        
    public List<DataSource> getDataSources(){
        List<DataSource> dataSourceList = new ArrayList<DataSource>();
        
        DataSource dataSource1 = new DataSource();
        dataSource1.setID("dataset1");
        dataSource1.setLabel("hello");
        dataSource1.setURI("http:hello");
        DataSource dataSource2 = new DataSource();
        dataSource2.setID("dataset2");
        dataSource2.setLabel("hello2");
        dataSource2.setURI("http:hello2");
        DataSource dataSource3 = new DataSource();
        dataSource3.setID("dataset2");
        dataSource3.setLabel("hello2");
        dataSource3.setURI("http:hello2");
        
        dataSourceList.add(dataSource1);
        dataSourceList.add(dataSource2);
        dataSourceList.add(dataSource3);
        
        return dataSourceList;
    }
    
}
