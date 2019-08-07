#!/bin/bash
# 把 Cookie 参数转换成 Cookie 文件
#-------------------------------------------------
# 命令执行示例：
# ./convert_cookie.sh -c "DedeUserID=47225816;DedeUserID__ckMd5=fac9e6f28b02a888;SESSDATA=73ff9248%2C1567777289%2C821d2781;bili_jct=e8e5029cc3b89f92a712a243577a13c3;"
#-------------------------------------------------

# 命令参数定义
export cookie=""             # -c: 完整 Cookie 串（k1=v1;k2=v2;...;kn=vn;），要求不能有空格
export DedeUserID=""         # -i: [DedeUserID] Cookie 参数
export DedeUserID__ckMd5=""  # -m: [DedeUserID__ckMd5] Cookie 参数
export SESSDATA=""           # -s: [SESSDATA] Cookie 参数
export bili_jct=""           # -b: [bili_jct] Cookie 参数
export Expires="30"          # -e: [Expires] Cookie 有效天数（默认 30 天）
export ctype="mini"          # -t: 生成 Cookie 类型（main: 主号; mini: 小号; vest: 马甲）
export cpath="./cookies"     # -o: 生成 Cookie 文件位置


# 使用说明
usage() 
{ 
  cat <<EOF
    -h                        This help message.
    -c <cookie>               Full Cookie string: k1=v1;k2=v2;...;kn=vn;
    -i <DedeUserID>           Cookie parameter.
    -m <DedeUserID__ckMd5>    Cookie parameter.
    -s <SESSDATA>             Cookie parameter.
    -b <bili_jct>             Cookie parameter.
    -e <Expires>              Cookie expires days. (Default: 30)
    -t <ctype>                Cookie type. (Eg: main, mini, vest. Default: mini)
    -o <cpath>                Save Cookies Directory. (Default: "./cookies")
EOF
  exit 0 
}
[ "$1" = "" ] && usage
[ "$1" = "-h" ] && usage
[ "$1" = "-H" ] && usage


# 定义参数键和值
set -- `getopt c:i:m:s:b:e:t:o: "$@"`
while [ -n "$1" ]
do
  case "$1" in 
    -c) cookie="$2"
        shift ;;
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


if [ -n "${cookie}" ] ; then
  OLD_IFS="$IFS"
  IFS=";"
  array=(${cookie})
  IFS="$OLD_IFS"

  for kv in ${array[@]}
  do
    if [[ ${kv} == DedeUserID__ckMd5* ]]; then
      DedeUserID__ckMd5=${kv#*=}

    elif [[ ${kv} == DedeUserID* ]] ; then
      DedeUserID=${kv#*=}

    elif [[ ${kv} == SESSDATA* ]] ; then
      SESSDATA=${kv#*=}

    elif [[ ${kv} == bili_jct* ]] ; then
      bili_jct=${kv#*=}

    fi
  done
fi


Expires=`date -d "-${Expires} days ago" "+%a, %d %b %Y %T GMT+08:00"`
echo "---------- Input Params ----------"
echo "cookie = ${cookie}"
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


