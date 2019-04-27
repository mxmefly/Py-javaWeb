import re

str = '转发微博'

str = str.replace(r'回复@(.*):',"")
list= re.findall(r'(回复@.*:)|(@.*\s?)',str)
str = re.sub(r'(回复@.*:)|(@.*\s?)|转发微博',"",str)

print(list)
print(str)