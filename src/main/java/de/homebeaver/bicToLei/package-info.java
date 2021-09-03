/*
 * BIC-to-LEI Relationship
 * 
 * In February 2018, GLEIF and SWIFT introduced the first relationship file 
 * that matches a BIC against its LEI.
 * 
 * see https://www.gleif.org/en/lei-data/lei-mapping/download-bic-to-lei-relationship-files
 * 
 * The file is published in comma-separated format (csv). 
 * It contains all BIC-LEI pairs, i.e. is a “full” file. 
 * No “delta” file is made available.
 * The file contains the following data: 
 * FIELD NAME | FORMAT          PRESENCE       DESCRIPTION
 * BIC        | 11 alphanumeric Always present The SWIFT BIC code of the entity. 
 * LEI        | 20 alphanumeric Always present The LEI of the entity
 * 
 * see 2019-07-15_bic-to-lei-factsheet_v1.2-final.pdf
 */
package de.homebeaver.bicToLei;