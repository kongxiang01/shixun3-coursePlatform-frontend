package com.icplatform.service;

import com.icplatform.entity.Assets;
import com.icplatform.repositories.AssetsRepositories;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class AssetsService {
    private AssetsRepositories assetsRepositories;

    public AssetsService(AssetsRepositories assetsRepositories) {
        this.assetsRepositories = assetsRepositories;
    }

    public Optional<String> searchTpathByFname(String fname) {
        return assetsRepositories.findTpathByFname(fname);
    }
    //更新资源信息
    public void updateAssetByFname(String fname, String type, long size, String tpath, Date time) {
        Assets asset = assetsRepositories.findByFname(fname)
                .orElseThrow(() -> new IllegalArgumentException("资产未找到"));
        asset.setTpath(tpath);
        asset.setType(type);  // 更新文件类型
        asset.setSize(size);  // 更新文件大小
        asset.setTime(time);  // 更新时间戳
        assetsRepositories.save(asset);  // 保存更新后的资产
    }
    //插入资源信息
    public void insertNewAsset(String fname, String type, long size, String tpath, Date time) {
        Assets asset = new Assets();
        asset.setFname(fname);
        asset.setType(type);
        asset.setSize(size);
        asset.setTpath(tpath);
        asset.setTime(time);
        assetsRepositories.save(asset);  // 保存新资产
    }

    public Assets getAssetByFname(String fname) {
        return  assetsRepositories.findByFname(fname).orElse(null);
    }

}
