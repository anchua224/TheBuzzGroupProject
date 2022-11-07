import { render, screen } from '@testing-library/react';
import Profile from '../Profile'

test('render the Profile',()=>{
    render(<Profile/>);
    const pro = screen.getByTestId('profile');
    expect(pro).toBeInTheDocument();
});