package org.example.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 错误尝试计数器工具类
 * 用于跟踪密码修改时的错误尝试次数
 */
public class PasswordAttemptTracker {
    // 存储用户密码错误尝试次数
    private static final Map<String, Integer> attemptCounts = new ConcurrentHashMap<>();
    // 存储用户锁定结束时间戳
    private static final Map<String, Long> lockoutTimestamps = new ConcurrentHashMap<>();

    /**
     * 记录一次错误尝试
     * @param username 用户名
     */
    public static void recordFailedAttempt(String username) {
        // 如果用户被锁定，先检查是否已解锁
        if (isLocked(username)) {
            return; // 用户仍处于锁定状态
        }

        int currentAttempts = attemptCounts.getOrDefault(username, 0);
        currentAttempts++;
        attemptCounts.put(username, currentAttempts);

        // 如果达到3次错误尝试，锁定用户5分钟
        if (currentAttempts >= 3) {
            lockoutTimestamps.put(username, System.currentTimeMillis() + 5 * 60 * 1000); // 5分钟
        }
    }

    /**
     * 检查用户是否被锁定
     * @param username 用户名
     * @return 如果被锁定返回true，否则返回false
     */
    public static boolean isLocked(String username) {
        Long lockoutTime = lockoutTimestamps.get(username);
        if (lockoutTime != null) {
            if (System.currentTimeMillis() < lockoutTime) {
                return true; // 仍在锁定时间内
            } else {
                // 锁定时间已过，清除锁定状态
                unlock(username);
                return false;
            }
        }
        return false;
    }

    /**
     * 获取剩余锁定时间（毫秒）
     * @param username 用户名
     * @return 剩余锁定时间，如果没有锁定则返回0
     */
    public static long getRemainingLockTime(String username) {
        Long lockoutTime = lockoutTimestamps.get(username);
        if (lockoutTime != null) {
            long remaining = lockoutTime - System.currentTimeMillis();
            return Math.max(0, remaining);
        }
        return 0;
    }

    /**
     * 清除用户的锁定状态
     * @param username 用户名
     */
    public static void unlock(String username) {
        attemptCounts.remove(username);
        lockoutTimestamps.remove(username);
    }

    /**
     * 获取当前错误尝试次数
     * @param username 用户名
     * @return 错误尝试次数
     */
    public static int getAttemptCount(String username) {
        if (isLocked(username)) {
            return 3; // 如果被锁定，则返回最大尝试次数
        }
        return attemptCounts.getOrDefault(username, 0);
    }

    /**
     * 重置用户的尝试次数（成功修改密码后调用）
     * @param username 用户名
     */
    public static void resetAttempts(String username) {
        attemptCounts.remove(username);
    }
}
