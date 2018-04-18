# Code source et manifests Kubernetes pour le [talk Mes Applications en Production sur Kubernetes](https://cfp.devoxx.fr/2018/talk/PMF-2419/Mes_Applications_en_production_sur_Kubernetes) 

## Construire l'image applicative

```bash
$ cd java
$ docker build .
```

## Préparer le cluster pour faire du HPA

Le [POD de médiation](https://hub.docker.com/r/directxman12/k8s-prometheus-adapter-amd64/) qui fait le lien entre Kubernetes et Prometheus est le travail de [DirectXMan12](https://github.com/DirectXMan12/k8s-prometheus-adapter)

Un [super blog d'Arnaud Mazin](https://blog.octo.com/comment-scaler-le-nombre-de-pods-dans-votre-cluster-kubernetes/) explique en Français comment ça marche.

## Les manifest Kubernetes

Ils se trouvent dans répertoire java :

* 0-k8s-lab-java-deployment.yaml : le pod de base, sans sécurité
* 1-k8s-lab-java-deployment.yaml : avec de la sécurité
* 2-k8s-lab-java-deployment.yaml : avec du contrôle de ressources et des tests de vie / readiness tests.
