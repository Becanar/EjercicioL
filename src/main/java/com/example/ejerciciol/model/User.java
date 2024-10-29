package com.example.ejerciciol.model;

import java.util.Objects;

public class User {
    private String usuario;
    private String password;

    public User(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public User() {}

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "usuario='" + usuario + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User u = (User) o;
        return Objects.equals(usuario, u.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(usuario);
    }

}
