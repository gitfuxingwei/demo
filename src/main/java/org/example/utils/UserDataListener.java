package org.example.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户数据导入监听器
 * 用于处理Excel数据读取过程中的校验逻辑
 */
@Slf4j
public class UserDataListener extends AnalysisEventListener<UserDTO> {
    private static final Logger logger = LoggerFactory.getLogger(UserDataListener.class);

    // 最大读取行数限制
    private static final int MAX_ROWS = 200;

    // 存储解析出来的数据
    private List<UserDTO> userDataList = new ArrayList<>();

    // 用于校验重复数据的集合
    private Set<String> userNameSet = new HashSet<>();
    private Set<String> phoneNumberSet = new HashSet<>();

    // 校验错误信息
    private List<String> errorMessages = new ArrayList<>();

    // 记录是否已达到最大行数限制
    private boolean maxRowsReached = false;

    @Override
    public void invoke(UserDTO userDTO, AnalysisContext analysisContext) {
        int currentRowIndex = analysisContext.readRowHolder().getRowIndex();

        // 检查是否超过最大行数限制
        if (currentRowIndex > MAX_ROWS) {
            // 如果还没有记录过最大行数超限的信息，则记录一次
            if (!maxRowsReached) {
                errorMessages.add("数据量过大：Excel文件包含超过 " + MAX_ROWS + " 行数据，系统仅处理前 " + MAX_ROWS + " 条数据");
                maxRowsReached = true;
            }
            // 不返回，继续处理但不添加到列表中
        } else {
            // 校验手机号不能为空
            if (userDTO.getPhonenumber() == null || userDTO.getPhonenumber().trim().isEmpty()) {
                errorMessages.add("第 " + (currentRowIndex + 1) +
                                 " 行：手机号不能为空");
            } else {
                // 对各个字段进行长度校验，防止数据库字段长度限制
                validateFieldLengths(userDTO, currentRowIndex + 1);

                // 校验用户名和手机号是否重复
                if (userDTO.getUserName() != null && !userDTO.getUserName().trim().isEmpty()) {
                    if (userNameSet.contains(userDTO.getUserName().toLowerCase())) {
                        errorMessages.add("第 " + (currentRowIndex + 1) +
                                         " 行：用户名重复 - " + userDTO.getUserName());
                    } else {
                        userNameSet.add(userDTO.getUserName().toLowerCase());
                    }
                }

                if (userDTO.getPhonenumber() != null && !userDTO.getPhonenumber().trim().isEmpty()) {
                    if (phoneNumberSet.contains(userDTO.getPhonenumber())) {
                        errorMessages.add("第 " + (currentRowIndex + 1) +
                                         " 行：手机号重复 - " + userDTO.getPhonenumber());
                    } else {
                        phoneNumberSet.add(userDTO.getPhonenumber());
                    }
                }

                // 如果没有错误，则添加数据
                if (errorMessages.isEmpty() || !hasErrorsInCurrentRow(errorMessages, currentRowIndex)) {
                    userDataList.add(userDTO);
                }
            }
        }
    }

    /**
     * 校验各字段长度，防止超出数据库字段限制
     */
    private void validateFieldLengths(UserDTO userDTO, int rowNum) {
        // 校验用户名长度
        if (userDTO.getUserName() != null && userDTO.getUserName().length() > 50) {
            errorMessages.add("第 " + rowNum + " 行：用户名长度超过50个字符限制");
        }

        // 校验用户类型
        if (userDTO.getUserType() != null) {

            if (userDTO.getUserType() < 0 || userDTO.getUserType() > 3) {
                errorMessages.add("第 " + rowNum + " 行：用户类型值超出有效范围");
            }
        }

        // 校验邮箱长度（假设数据库中限制为255字符）
        if (userDTO.getEmail() != null && userDTO.getEmail().length() > 255) {
            errorMessages.add("第 " + rowNum + " 行：邮箱长度超过255个字符限制");
        }

        // 校验手机号长度
        if (userDTO.getPhonenumber() != null && userDTO.getPhonenumber().length() != 11) {
            errorMessages.add("第 " + rowNum + " 行：手机号长度不符合11位要求");
        }

        // 校验性别长度）
        if (userDTO.getSex() != null && userDTO.getSex().length() > 1) {
            errorMessages.add("第 " + rowNum + " 行：性别字段长度超过字符限制");
        }

        // 校验状态长度
        if (userDTO.getStatus() != null && userDTO.getStatus().length() > 1) {
            errorMessages.add("第 " + rowNum + " 行：状态字段长度超过字符限制");
        }

        // 校验备注长度
        if (userDTO.getRemark() != null && userDTO.getRemark().length() > 500) {
            errorMessages.add("第 " + rowNum + " 行：备注长度超过200个字符限制");
        }
    }

    /**
     * 检查当前行是否有错误
     */
    private boolean hasErrorsInCurrentRow(List<String> errorMessages, int rowIndex) {
        String rowErrorIndicator = "第 " + (rowIndex + 1) + " 行：";
        for (String errorMsg : errorMessages) {
            if (errorMsg.startsWith(rowErrorIndicator)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 获取解析的数据
     */
    public List<UserDTO> getUserDataList() {
        return userDataList;
    }

    /**
     * 检查是否有错误
     */
    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }

    /**
     * 获取错误信息
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
