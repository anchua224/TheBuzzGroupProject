import { render, screen } from '@testing-library/react';
import Ideas from '../Ideas';

test('render the login part',()=>{
    render(<Login/>);
    const loginpart = screen.getByTestId('login');
    expect(loginpart).toBeInTheDocument();
});