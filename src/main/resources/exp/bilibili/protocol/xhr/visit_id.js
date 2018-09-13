/**
 * 节奏风暴的入参visit_id的生成算法.
 * @returns 只含有 [0-9a-z] 的长度为12的随机字符串
 */
function get_visit_id() {
	// (new Date).getTime() 为当前时间的毫秒值，如: 1526140830824
	// Math.ceil(x) 表示对浮点数x向上取整, 即返回大于参数x的最小整数
	// Math.random() 返回0.0 ~ 1.0 之间的一个伪随机数, 如： 0.7033043201427198
	// toString(36) 表示生成36进制字符串, 亦即只含有 [0-9a-z] 的字符串
    return ((new Date).getTime() * Math.ceil(1e6 * Math.random())).toString(36)
}