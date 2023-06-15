package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Provider;

public class SingletonWithPrototype {

    @Scope("prototype")
    @Component
    static class PrototypeBean {
        @Test
        void PrototypeFind() {
            AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
            PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
            prototypeBean1.addCount();
            Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

            PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
            prototypeBean2.addCount();
            Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);
        }

        @Test
        void SingletonClientUserPrototype(){
            AnnotationConfigApplicationContext ac =
                    new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

            ClientBean clientBean1 = ac.getBean(ClientBean.class);
            int count1 = clientBean1.logic();
            Assertions.assertThat(count1).isEqualTo(1);

            ClientBean clientBean2 = ac.getBean(ClientBean.class);
            int count2 = clientBean2.logic();
            Assertions.assertThat(count2).isEqualTo(1);
        }

        @Scope("singleton")
        @Component
        static class ClientBean {

            @Autowired
            private Provider<PrototypeBean> prototypeBeanProvider;

//            @Autowired
//            public ClientBean(PrototypeBean prototypeBean) {
//                this.prototypeBean = prototypeBean;
//            }

            public int logic(){
                PrototypeBean prototypeBean = prototypeBeanProvider.get();
                prototypeBean.addCount();
                int count = prototypeBean.getCount();
                return count;
            }
        }

        private int count = 0;

        public void addCount(){
            count ++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy " + this);
        }
    }
}
