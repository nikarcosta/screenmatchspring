package com.nika.screenmatchspring.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
