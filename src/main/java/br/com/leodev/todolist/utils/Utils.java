package br.com.leodev.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target){ // Transformar uma classe em static faz com que não seja necessário instanciá-la para poder acessá-la.
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source)); // Método copyProperties() do BeanUtils permite informar um objeto como fonte para cópia dos dados, um objeto alvo para receber a cópia destes dados e ainda uma classe que servirá de "regra" para a cópia dos dados entre os objetos.
    }
    
    public static String[] getNullPropertyNames(Object source){ // TipoDeDado[]: Sintaxe para criação de um array em Java.
        final BeanWrapper src = new BeanWrapperImpl(source); // Interace nativa do java que possuí métodos pré-definidos para manipulação de objetos. BenWrapperImpl representa sua implementação, a instanciação do objeto da classe/interface.

        PropertyDescriptor[] pds = src.getPropertyDescriptors(); // Capturando as propriedades de um objeto;

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd:pds ){ // Para (Tipo PropertyDescriptor chamado pd existente no array pds). Essa é, aproximadamente, a leitura da sintaxe ao lado.
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    
}
