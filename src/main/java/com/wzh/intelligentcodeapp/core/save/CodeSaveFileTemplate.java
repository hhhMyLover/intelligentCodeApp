package com.wzh.intelligentcodeapp.core.save;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;

import java.io.File;

public abstract class CodeSaveFileTemplate<T> {

    public static final String ROOT_DIR_PATH = System.getProperty("user.dir") + "/tem/code_output";


    public File saveCode(T result){
        // 验证参数
        validateParam(result);
        // 构造文件目录
        String dirPath = buildUniqueFileName(getBizType());
        // 保存文件
        saveFile(result,dirPath);
        return new File(dirPath);
    }

    protected void validateParam(T result) {
        if (ObjectUtil.isEmpty(result)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "保存代码为空");
        }
    }

    protected final void writeFile(String dirPath, String fileName, String content) {
        File file = new File(dirPath+ File.separator + fileName);
        FileUtil.writeUtf8String(content, file);
    }

    protected String buildUniqueFileName(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}",bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = ROOT_DIR_PATH + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }


    protected abstract void saveFile(T result, String dirPath);

    protected abstract String getBizType();

}
