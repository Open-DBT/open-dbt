package com.highgo.opendbt.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * @Description: pg关键字转换
 * @Title: PostgresqlKeyWord
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
public class PostgresqlKeyWord {

	static Logger logger = LoggerFactory.getLogger(PostgresqlKeyWord.class);

	private final static String PostgresqlPattern = "^[A-Za-z_][A-Za-z0-9_$]*$";

	public static void main(String[] args) {
		String a = convertorToPostgresql("join");
		System.out.println(a);
	}

	public static String convertorToPostgresql(String name) {
		if (!name.matches(PostgresqlPattern)) {
			name = "\"" + name + "\"";
			logger.info(name + " not match HgNamePattern");
			return name;
		}

		List<String> objTabColNameKeyWords = getHighgoKeyWords();
		for (String keyWord : objTabColNameKeyWords) {
			if (name.equalsIgnoreCase(keyWord)) {
				name = "\"" + name + "\"";
				logger.info(name + " is ReservedKeyWord");
				return name;
			}
		}

		return name;
	}

	private static List<String> getHighgoKeyWords() {
		List<String> keywords = new ArrayList<String>();

		keywords.add("AUTHORIZATION");
		keywords.add("BINARY");
		keywords.add("COLLATION");
		keywords.add("CONCURRENTLY");
		keywords.add("CROSS");
		keywords.add("CURRENT_SCHEMA");
		keywords.add("FREEZE");
		keywords.add("FULL");
		keywords.add("ILIKE");
		keywords.add("INNER");
		keywords.add("IS");
		keywords.add("ISNULL");
		keywords.add("JOIN");
		keywords.add("LEFT");
		keywords.add("LIKE");
		keywords.add("NATURAL");
		keywords.add("NOTNULL");
		keywords.add("OUTER");
		keywords.add("OVERLAPS");
		keywords.add("RIGHT");
		keywords.add("SIMILAR");
		keywords.add("TABLESAMPLE");
		keywords.add("VERBOSE");

		keywords.add("ALL");
		keywords.add("ANALYSE");
		keywords.add("ANALYZE");
		keywords.add("AND");
		keywords.add("ANY");
		keywords.add("ARRAY");
		keywords.add("AS");
		keywords.add("ASC");
		keywords.add("ASYMMETRIC");
		keywords.add("BOTH");
		keywords.add("CASE");
		keywords.add("CAST");
		keywords.add("CHECK");
		keywords.add("COLLATE");
		keywords.add("COLUMN");
		keywords.add("CONSTRAINT");
		keywords.add("CREATE");
		keywords.add("CURRENT_CATALOG");
		keywords.add("CURRENT_DATE");
		keywords.add("CURRENT_ROLE");
		keywords.add("CURRENT_TIME");
		keywords.add("CURRENT_TIMESTAMP");
		keywords.add("CURRENT_USER");
		keywords.add("DEFAULT");
		keywords.add("DEFERRABLE");
		keywords.add("DESC");
		keywords.add("DISTINCT");
		keywords.add("DO");
		keywords.add("ELSE");
		keywords.add("END");
		keywords.add("EXCEPT");
		keywords.add("FALSE");
		keywords.add("FETCH");
		keywords.add("FOR");
		keywords.add("FOREIGN");
		keywords.add("FROM");
		keywords.add("GRANT");
		keywords.add("GROUP");
		keywords.add("HAVING");
		keywords.add("IN");
		keywords.add("INITIALLY");
		keywords.add("INTERSECT");
		keywords.add("INTO");
		keywords.add("LATERAL");
		keywords.add("LEADING");
		keywords.add("LIMIT");
		keywords.add("LOCALTIME");
		keywords.add("LOCALTIMESTAMP");
		keywords.add("NOT");
		keywords.add("NULL");
		keywords.add("OFFSET");
		keywords.add("ON");
		keywords.add("ONLY");
		keywords.add("OR");
		keywords.add("ORDER");
		keywords.add("PLACING");
		keywords.add("PRIMARY");
		keywords.add("REFERENCES");
		keywords.add("RETURNING");
		keywords.add("SELECT");
		keywords.add("SESSION_USER");
		keywords.add("SOME");
		keywords.add("SYMMETRIC");
		keywords.add("TABLE");
		keywords.add("THEN");
		keywords.add("TO");
		keywords.add("TRAILING");
		keywords.add("TRUE");
		keywords.add("UNION");
		keywords.add("UNIQUE");
		keywords.add("USER");
		keywords.add("USING");
		keywords.add("VARIADIC");
		keywords.add("VERBOSE");
		keywords.add("WHEN");
		keywords.add("WHERE");
		keywords.add("WINDOW");
		keywords.add("WITH");

		return keywords;
	}

}
