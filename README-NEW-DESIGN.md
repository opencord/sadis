CHANGES
-------------------

- api package:

    - New SadisService that introduces / encapsulates the information-based services is added.

    - The oldest SubscriberAndDeviceInformationService name is changed as BaseInformationService. This
    service includes the common methods / APIs for the information classes. The older methods still remain.
    In order to support common methods for different information classes, this interface is made Generic.

    - We assume that each information class must have an unique id. So, this field is moved to a new class 
    that is called as BaseInformation.

    - According to the previous item, SubscriberAndDeviceInformation class extends BaseInformation.

    - BandwidthProfileInformation class that includes bandwidth profile specific fields such as PIR, CIR,
    EIR is added and it also extends BaseInformation as well.

    - We assume that each config class will have remote url, maximum cache size and cache ttl value. Also,
    each local or remote config will have specific entries. So, BaseConfig abstract class is added and it 
    must be extended by specific configuration classes.

- cli package:

    -  New CLI class BandwidthProfileGetCommand is added to get bandwidth profile context. The usage of the
    cli command:
        bandwidthProfile bandwidth-profile-id

- impl package:

    - BandwidthProfileCodec is added to encode the related bandwidth profile json entries.

    - BandwidthProfileConfig class that extends BaseConfig is added.

    - SubscriberAndDeviceInformationConfig class is changed extending BaseConfig.

    - InformationAdapter class is made more Generic. It includes common methods for different information
    classes.

    - SubscriberManager and BandwidthProfileManager classes extend InformationAdapter and implements
    specific methods.

    - SadisManager is the implementation of SadisService and it can be used by different applications as
    an OSGI service. When this service is activated, it create specific services such as SubscriberManager
    and BandwidthProfileManager and handles their registrations.

- rest package:

    - New BandwidthProfileWebResource class is added to use bandwidth profiles via REST api.

- test package:

    - Unit test is added for BandwidthProfileManager class.

TEST
-------
To test the related work:

- Run ONOS 1.13.6 using "ok clean"

- Go to 1.13.6/config directory and create cfg.json. If you want to test the remote address, then enter the entry:

 ```
    {
       "apps":{
          "org.opencord.sadis":{
             "sadis":{
                "integration":{
                   "url":"http://localhost:8222/%s",
                   "cache":{
                      "enabled":true,
                      "maxsize":20,
                      "ttl":"PT1m"
                   }
                }
             },
            "bandwidthprofile" : {
                "integration" : {
                    "url" : "http://localhost:8222/%s",
                    "cache":{
                      "enabled":true,
                      "maxsize":40,
                      "ttl":"PT1m"
              }
          }
         }
       }
     }
    }
```

 If you test the local data, then enter an entry such like that:

  ```
  {
     "apps":{
        "org.opencord.sadis":{
           "sadis":{
              "integration":{
                 "cache":{
                    "enabled":true,
                    "maxsize":50,
                    "ttl":"PT1m"
                 }
              },
              "entries":[
                 {
                    "id":"uni-128",
                    "cTag":2,
                    "sTag":2,
                    "nasPortId":"uni-128",
                    "technologyProfileId":64,
                    "upstreamBandwidthProfile":"High-Speed-Internet",
                    "downstreamBandwidthProfile":"User1-Specific"
                 },
                 {
                    "id":"1d3eafb52e4e44b08818ab9ebaf7c0d4",
                    "hardwareIdentifier":"00:1b:22:00:b1:78",
                    "ipAddress":"192.168.1.252",
                    "nasId":"B100-NASID"
                 }
              ]
           },
           "bandwidthprofile":{
              "integration":{
                 "cache":{
                    "enabled":true,
                    "maxsize":40,
                    "ttl":"PT1m"
                 }
              },
              "entries":[
                 {
                    "id":"High-Speed-Internet",
                    "cir":200000000,
                    "cbs":348000,
                    "eir":10000000,
                    "ebs":348000,
                    "air":10000000
                 },
                 {
                    "id":"User1-Specific",
                    "cir":300000000,
                    "cbs":348000,
                    "eir":20000000,
                    "ebs":348000,
                    "air":30000000
                 }
              ]
           }
        }
     }
  }
  ```

- Install Cord-Config Oar - Be sure that this application ONOS dependency is also 1.13.6.
  ```
    onos-app localhost install ~/voltha-projects/config/target/cord-config-1.5.0-SNAPSHOT.oar
    ```
- Install and activate SADIS.
  ```
    onos-app localhost install ~/voltha-projects/sadis-new/sadis/app/target/sadis-app-2.2.0-SNAPSHOT.oar
    onos-app localhost activate org.opencord.sadis
   ```
- Add the config that refers to cfg.json.
  ```
    onos-netcfg localhost /tmp/1.13.6/config/cfg.json
   ```

- Connect to ONOS CLI and run SADIS commands:
  ```
    sadis subscriber-id
    bandwidthProfile bandwidth-profile-id
  ```

USAGE OF REMOTE SERVER

- If you want to test the remote address connection, then you can use NGINX server.

- Install Docker and Docker-Compose

- Create the volume data

```
    sudo mkdir /var/sadis_data
    cd sadis_data
    sudo nano uni-2

    enter the following:
    {
      "id": "uni-002",
      "cTag": 2,
      "sTag": 2,
      "technologyProfileId" : 64,
      "upstreamBandwidthProfile" : "High-Speed",
      "downstreamBandwidthProfile" : "High-Speed"
    }

    sudo nano HighSpeed

    enter the following:
   {
      "id": "High-Speed",
      "cir": 1000000000,
      "cbs": 384000,
      "eir": 100000000,
      "ebs": 384000,
      "air": 100000000
   }

```

- Create the docker volume

```
    docker volume create --name sadis_data
    docker create --name helper --volume sadis_data:/sadis_data busybox true
    docker cp /var/sadis_data/. helper:/sadis_data/
    docker rm helper
```

- Clone NGINX server

```
    cd ~/voltha-projects
    git clone https://github.com/indiehosters/nginx
    cd nginx
```

- Change the Docker-Compose file like this

```
version: "3.0"
services:
    sadis:
        image: "nginx"
        ports:
            - 8222:8222
        # Move the port from 80 to 8222, on which nginx listens
        command: /bin/bash -c "cp /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.orig && sed -e 's/80/8222/g' /etc/nginx/conf.d/default.conf.orig | envsubst > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"
        deploy:
            replicas: 1
            placement:
                constraints:
                    - node.role == manager
        volumes:
            - sadis_data:/usr/share/nginx/html:ro
volumes:
    sadis_data:
        external:
            name: sadis_data
```

- Start the NGINX docker

```
docker-compose up -d
sudo docker ps -a (nginx status must be up)
```

- If you want to use two different NGINX docker for subscriber and bandwidth info separately,
then you can create two different volume (one includes only subscriber files and the other includes only bandwidth profile), and after then you can change the docker-compose file
regarding to the volume and forwarding port.

EXTEND
------
- If you want to add a new information to SADIS:

    - Add the related Information class that extends BaseInformation such as BandwidthProfileInformation

    - Add the related Config class that extends BaseConfig such as BandwidthProfileConfig

    - Add the related Codec class that is used to encode related JSON

    - Update SadisService - add a new method for the new information

    - Create Information-Based-Manager class - it must extend InformationAdapter

    - Update SadisManager based on the changed API and the newly added manager

    - Add CLI and REST API for the related information

    - Add unit test for the new information manager class
