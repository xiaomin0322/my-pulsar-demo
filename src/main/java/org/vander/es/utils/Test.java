package org.vander.es.utils;

import java.util.Date;

import lombok.Data;

/**
 * { "_index": "library", "_type": "book", "_id": "mXNQvHIB-AABCyFDxkqY",
 * "_version": 1, "_score": null, "_source": { "name": "Dune", "author": "Frank
 * Herbert", "release_date": "1965-06-01", "page_count": 604 }, "sort": [ 604 ]
 * }
 * 
 * @author Zengmin.Zhang
 *
 */
@Data
public class Test {

	private String name;

	private String author;

	private Long page_count;

	private Date release_date;
}
