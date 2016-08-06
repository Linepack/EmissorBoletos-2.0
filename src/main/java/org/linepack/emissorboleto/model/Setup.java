/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Leandro
 */
@Entity
@Table(name = "bol_config")
@NamedQuery(name = "setupAtivo", query = "select s from Setup s where s.isAtivo = 1")
public class Setup extends BaseModel {

    @Column(name = "ds_ip_servico", nullable = false)
    private String ip;
    @Column(name = "ds_host_servico", nullable = false)
    private String host;
    @Column(name = "ds_porta_servico", nullable = false)
    private String port;
    @Column(name = "ds_path_to_save")
    private String pathToSaveFile;
    @Column(name = "st_ativo")
    private Integer isAtivo;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPathToSaveFile() {
        return pathToSaveFile;
    }

    public void setPathToSaveFile(String pathToSaveFile) {
        this.pathToSaveFile = pathToSaveFile;
    }

    public Integer getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Integer isAtivo) {
        this.isAtivo = isAtivo;
    }

}
