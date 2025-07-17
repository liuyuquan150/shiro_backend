export function generateUsername() {
    return `user_${Math.floor(Math.random() * 10000)}`;
}

export function generateChinesePhoneNumber() {
    // 生成手机号的第 1 位, 固定为 1.
    const firstDigit = '1';
    // 生成手机号的第 2 位, 从 3 到 9 中随机选择一个数字.
    const secondDigit = Math.floor(Math.random() * (9 - 3 + 1)) + 3;
    // 生成手机号的后 9 位, 每 1 位都是从 0 到 9 中随机选择的数字.
    let lastNineDigits = '';
    for (let i = 0; i < 9; i++) {
        // 生成一个随机数字, 范围从 0 到 9.
        lastNineDigits += Math.floor(Math.random() * 10);
    }
    // 将三部分拼接起来, 形成一个完整的手机号码.
    return `${firstDigit}${secondDigit}${lastNineDigits}`;
}