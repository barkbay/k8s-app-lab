FROM golang:1.9.2
WORKDIR $HOME/Documents/Devoxx/cfp-2018/k8s-app-lab
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o k8s-app-lab .

FROM scratch
COPY --from=0 $HOME/Documents/Devoxx/cfp-2018/k8s-app-lab/k8s-app-lab .
ENTRYPOINT ["/k8s-app-lab"]
