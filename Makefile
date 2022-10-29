CP=$(shell clojure -A:dev -Spath)

.PHONY: lint
lint:
	cljstyle check
	clj-kondo --lint src:test

.PHONY: test
test: test-clj test-bb test-nbb

.PHONY: test-clj
test:
	clojure -M:dev:test

.PHONY: test-bb
test-bb:
	@bb --classpath "$(CP)" bb_test_runner.clj

.PHONY: test-nbb
test-nbb:
	@npx nbb --classpath "$(CP)" bb_test_runner.clj

.PHONY: outdated
outdated:
	clojure -M:outdated --upgrade

.PHONY: clean
clean:
	rm -rf .cpcache target
