# PHAT Example Variation Activity [01]
The patient performs basic activities within his home.
The activities are defined within the configuration file where it is possible to indicate several aspects of the character's behavior:

- Home
- Avatar
- Sensors
- Sensors type


When performing activities, this is monitored with acceleration sensors in both hands.
<table>
<tr>
    <td>  
To run the demo

```
mvn clean compile
mvn exec:java -Dexec.mainClass=phat.ActvityVariationDemo
```
In case of running into memory problems
```
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=128m"
```
And then run the previous command
    </td>
    <td>
        <img src="https://github.com/mfcardenas/phat_example_variation_01/blob/master/img/img_older_people_home.png" />
    </td>
</tr>
</table>