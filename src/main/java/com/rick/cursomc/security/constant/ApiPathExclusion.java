package com.rick.cursomc.security.constant;

public class ApiPathExclusion {

    public enum GetApiPathExclusion {
        PRODUTOS("/produtos/**"), CATEGORIAS("/categorias/**");
        private final String path;

        GetApiPathExclusion(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }
    }


    public enum PostApiPathExclusion {

    }
    public enum PutApiPathExclusion {

    }
    public enum DeleteApiPathExclusion {

    }
}
