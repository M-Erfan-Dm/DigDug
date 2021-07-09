package ir.ac.kntu.services;

import ir.ac.kntu.exceptions.InvalidMapException;

import java.util.List;

public interface MapParserDao {
    List<Integer>[][] parse() throws InvalidMapException;
}
