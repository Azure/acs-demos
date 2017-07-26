# Uninstall Vamp

## Delete etcd

```
kubectl delete -f https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/etcd.yml
```

Results:

```
service "etcd-client" deleted
pod "etcd0" deleted
service "etcd0" deleted
pod "etcd1" deleted
service "etcd1" deleted
pod "etcd2" deleted
service "etcd2" deleted
```

## Delete ElasticSearch

```
kubectl delete deployment elasticsearch
```

Results:

```
deployment "elasticsearch" deleted
```

```
kubectl delete service elasticsearch
```

Results:

```
service "elasticsearch" deleted
```


```
kubectl delete deployment kibana
```

Results:

```
deployment "kibana" deleted
```

```
kubectl delete service kibana
```

Results:

```
service "kibana" deleted
```

## Delete Vamp

```
kubectl delete -f https://raw.githubusercontent.com/magneticio/vamp.io/master/static/res/v0.9.4/vga.yml
```

Results:

```
daemonset "vamp-gateway-agent" deleted
service "vamp-gateway-agent" deleted
```

```
kubectl delete deployment vamp
```

Results:

```
deployment "vamp" deleted
```

```
kubectl delete service vamp
```

Results:

```
service "vamp" deleted
```
