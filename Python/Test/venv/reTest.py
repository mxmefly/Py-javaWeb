import re
from snownlp import SnowNLP

print(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
str = '新年第一声我爱你！要讲给你！@田喜碧Hebe'

s=SnowNLP(str);
a=s.words;
print(a)

str = str.replace(r'回复@(.*):',"")
list= re.findall(r'(回复@.*:)|(@.*\s?)',str)
str = re.sub(r'(回复@.*:)|(@.*\s?)|转发微博',"",str)

print(list)
print(str)