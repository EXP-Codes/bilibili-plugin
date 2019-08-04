#!/bin/bash
# 把 Cookie 参数转换成 Cookie 文件
#-------------------------------------------------
# 命令执行示例：
# ./convert_cookie.sh -u USERNAME -p PASSWORD
#-------------------------------------------------

# 命令参数定义
export DedeUserID=""         # -i: [DedeUserID] Cookie 参数
export DedeUserID__ckMd5=""  # -m: [DedeUserID__ckMd5] Cookie 参数
export SESSDATA=""           # -s: [SESSDATA] Cookie 参数
export bili_jct=""           # -b: [bili_jct] Cookie 参数
export Expires="30"          # -e: [Expires] Cookie 有效天数（默认 30 天）
export ctype="main"          # -t: 生成 Cookie 类型（main: 主号; mini: 小号; vest: 马甲）
export cpath="./cookies"     # -o: 生成 Cookie 文件位置


# 使用说明
usage() 
{ 
  cat <<EOF
    -h                        This help message.
    -i <DedeUserID>           Cookie parameter.
    -m <DedeUserID__ckMd5>    Cookie parameter.
    -s <SESSDATA>             Cookie parameter.
    -b <bili_jct>             Cookie parameter.
    -e <Expires>              Cookie expires days. (Default: 30)
    -t <ctype>                Cookie type. (Eg: main, mini, vest)
    -o <cpath>                Save Cookies Directory. (Default: "./cookies")
EOF
  exit 0 
}
[ "$1" = "" ] && usage
[ "$1" = "-h" ] && usage
[ "$1" = "-H" ] && usage


# 定义参数键和值
set -- `getopt i:m:s:b:e:t:o: "$@"`
while [ -n "$1" ]
do
  case "$1" in 
    -i) DedeUserID="$2"
        shift ;;
    -m) DedeUserID__ckMd5="$2"
        shift ;;
    -s) SESSDATA="$2"
        shift ;;
    -b) bili_jct="$2"
        shift ;;
    -e) Expires="$2"
        shift ;;
    -t) ctype="$2"
        shift ;;
    -o) cpath="$2"
        shift ;;
  esac
  shift
done


Expires=`date -d "-${Expires} days ago" "+%a, %d %b %Y %T GMT+08:00"`
echo "---------- Input Params ----------"
echo "DedeUserID = ${DedeUserID}"
echo "DedeUserID__ckMd5 = ${DedeUserID__ckMd5}"
echo "SESSDATA = ${SESSDATA}"
echo "bili_jct = ${bili_jct}"
echo "Expires = ${Expires}"
echo "Cookie Type = ${ctype}"
echo "Save Directory = ${cpath}"
echo "----------------------------------"


Content=`cat <<EOF
DedeUserID=${DedeUserID} ; Domain=.bilibili.com ; Path=/ ; Expires=${Expires} ; 
DedeUserID__ckMd5=${DedeUserID__ckMd5} ; Domain=.bilibili.com ; Path=/ ; Expires=${Expires} ; 
SESSDATA=${SESSDATA} ; Domain=.bilibili.com ; Path=/ ; Expires=${Expires} ; HttpOnly ; 
bili_jct=${bili_jct} ; Domain=.bilibili.com ; Path=/ ; Expires=${Expires} ; 
EOF
`

mkdir ${cpath} 2>/dev/null
if [ ${ctype} = "mini" ] ; then
  cpath="${cpath}/cookie-${ctype}-${DedeUserID}.dat"

elif [ ${ctype} = "main" ] ; then
  cpath="${cpath}/cookie-${ctype}.dat"

elif [ ${ctype} = "vest" ] ; then
  cpath="${cpath}/cookie-${ctype}.dat"

else
  echo "Error Cookie Type : ${ctype}"
  exit 1
fi

echo "${Content}" > ${cpath}
echo "Finish: Cookie file has been save to : ${cpath}"
exit 0


