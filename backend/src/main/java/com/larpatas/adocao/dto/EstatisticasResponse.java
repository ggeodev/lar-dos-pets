package com.larpatas.adocao.dto;

/** Números exibidos na página inicial. Só contagens: nenhum dado pessoal. */
public record EstatisticasResponse(long disponiveis, long adotados, long cidades) { }
